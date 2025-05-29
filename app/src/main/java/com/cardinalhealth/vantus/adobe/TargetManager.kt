package com.cardinalhealth.vantus.adobe

import com.adobe.marketing.mobile.AdobeCallback
import com.adobe.marketing.mobile.Target
import com.adobe.marketing.mobile.target.TargetParameters
import com.adobe.marketing.mobile.target.TargetPrefetch
import com.adobe.marketing.mobile.target.TargetRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

object TargetManager {
    private const val MBOX_NAME = "homepage-offer"
    private const val DEFAULT1 = "DefaultValue1"

    private val _contentState = MutableStateFlow<ContentState>(ContentState.Loading)
    val contentState: StateFlow<ContentState> = _contentState

    sealed class ContentState {
        object Loading : ContentState()
        data class Success(val htmlContent: String) : ContentState()
        data class Error(val message: String) : ContentState()
    }

    fun prefetchContent() {
        val latch = CountDownLatch(1)

        val mboxParameters: HashMap<String?, String?> = hashMapOf(
            "name" to "Home_Zone0"
        )

        val profileParameters: HashMap<String?, String?> = hashMapOf(
            "User_Profile_DeliveryPlant" to "P003",
            "User_Profile_AccountNumber" to "2052008938",
            "User_Profile_CustomerMarketingCodes" to "92,408,865,861",
            "User_Profile_Affiliation" to "7007004219",
            "User_Profile_PDP" to " ",
            "User_Profile_State" to "NY",
            "User_Profile_Zip" to "10954-2050",
            "User_Profile_Customer_Classification" to "11",
            "User_Profile_Customer_Group" to "1I",
            "User_Profile_Customer_Group1" to "",
            "User_Profile_Industry_Key" to "MA",
            "User_Profile_Customer_Primary_Business_Unit" to "06",
            "User_Disable_Search" to "N",
            "User_Page_Type" to "home"
        )

        val targetParameters = TargetParameters
            .Builder()
            .parameters(mboxParameters)
            .profileParameters(profileParameters)
            .build()

        val prefetchObject = TargetPrefetch("Home_Zone0", targetParameters)

        val prefetchList = listOf(prefetchObject)

        Target.prefetchContent(prefetchList, targetParameters) { success ->
            if (success != null) {
                retrievePrefetchedContent()
            } else {
                _contentState.value = ContentState.Error("Prefetch failed")
            }
            latch.countDown()
        }
        latch.await(5, TimeUnit.SECONDS)
    }

    private fun retrievePrefetchedContent() {
        val latch = CountDownLatch(3)
        val callbackData = arrayOfNulls<String>(3)
        val adobeCallback = AdobeCallback<String> { data: String ->
            callbackData[0] = data
            latch.countDown()
        }

        val request = TargetRequest(
            MBOX_NAME,
            null,
            DEFAULT1,
            adobeCallback,
        )
        Target.retrieveLocationContent(listOf(request), null)
        Target.displayedLocations(listOf(MBOX_NAME), null)
    }

    fun clearCache() {
        Target.clearPrefetchCache()
    }
}