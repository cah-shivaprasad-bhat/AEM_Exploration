package com.cardinalhealth.vantus.adobe

import android.util.Log
import com.adobe.marketing.mobile.AdobeError
import com.adobe.marketing.mobile.Target
import com.adobe.marketing.mobile.target.AdobeTargetDetailedCallback
import com.adobe.marketing.mobile.target.TargetParameters
import com.adobe.marketing.mobile.target.TargetPrefetch
import com.adobe.marketing.mobile.target.TargetRequest

object TargetManager {
    private const val MBOX_NAME = "Home_Zone1"
    private const val DEFAULT = "DefaultValue1"

    private var mboxParameters = mutableMapOf<String, String>()
    private var profileParameters = mutableMapOf<String, String>()
    private var targetParameters: TargetParameters? = null

    fun prefetchContent() {
        mboxParameters["name"] = MBOX_NAME
        profileParameters.putAll(
            mapOf()
        )

        targetParameters = TargetParameters
            .Builder()
            .parameters(mboxParameters)
            .profileParameters(profileParameters)
            .build()

        val prefetchObject = TargetPrefetch(MBOX_NAME, targetParameters)

        val prefetchList = listOf(prefetchObject)

        Target.prefetchContent(prefetchList, targetParameters) {
            if (it == null) {
                println("Success")
                retrievePrefetchedContent()
            } else {
                println("Prefetch failed")
            }
        }
    }

    private fun retrievePrefetchedContent() {
        val request = TargetRequest(
            MBOX_NAME,
            targetParameters,
            DEFAULT,
            object : AdobeTargetDetailedCallback {
                override fun call(default: String?, data: MutableMap<String, Any>?) {
                    println("Default content: $default")
                    println("Data: $data")
                }

                override fun fail(e: AdobeError?) {
                    Log.d("ADOBE_MANAGER", "Exception = ${e?.errorName}")
                }

            }
        )

        Target.retrieveLocationContent(listOf(request), targetParameters)
        Target.displayedLocations(listOf(MBOX_NAME), targetParameters)
    }
}