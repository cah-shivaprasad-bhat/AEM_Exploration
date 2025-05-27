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
        val mboxParameters: HashMap<String?, String?> = object : HashMap<String?, String?>() {
            init {
                put("name", "Home_Zone0")
            }
        }
        val profileParameters: HashMap<String?, String?> = object : HashMap<String?, String?>() {
            init {

                put("User_Profile_DeliveryPlant", "P003")
                put("User_Profile_AccountNumber", "2052008938")
                put("User_Profile_CustomerMarketingCodes", "92,408,865,861")
                put("User_Profile_Affiliation", "7007004219")
                put("User_Profile_PDP", " ")
                put("User_Profile_State", "NY")
                put("User_Profile_Zip", "10954-2050")
                put("User_Profile_Customer_Classification", "11")
                put("User_Profile_Customer_Group", "1I")
                put("User_Profile_Customer_Group1", "")
                put("User_Profile_Industry_Key", "MA")
                put("User_Profile_Customer_Primary_Business_Unit", "06")
                put("User_Disable_Search", "N")
                put("User_Page_Type", "home")

            }
        }
        val targetParameters = TargetParameters
            .Builder()
            .parameters(mboxParameters)
            .profileParameters(profileParameters)
            .build()

        val prefetchObject = TargetPrefetch("Home_Zone0", targetParameters)

        val prefetchList = listOf(prefetchObject)

        Target.prefetchContent(prefetchList, targetParameters, object : AdobeCallback<String> {
            override fun call(success: String?) {
                if (success != null) {
                    retrievePrefetchedContent()
                } else {
                    _contentState.value = ContentState.Error("Prefetch failed")
                }
                latch.countDown()
            }
        })
        latch.await(10, TimeUnit.SECONDS)
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