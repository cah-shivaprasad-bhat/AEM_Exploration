package com.cardinalhealth.vantus.adobe

import android.app.Application
import android.content.Context
import com.adobe.marketing.mobile.Analytics
import com.adobe.marketing.mobile.Identity
import com.adobe.marketing.mobile.LoggingMode
import com.adobe.marketing.mobile.MobileCore
import com.adobe.marketing.mobile.Target
import javax.inject.Inject

class AdobeManager @Inject constructor(
    val context: Context
) {
    companion object {
        var mid = ""
    }

    fun init() {
        MobileCore.setApplication(context.applicationContext as Application)
        MobileCore.setLogLevel(LoggingMode.VERBOSE)
        try {
            val extensions = listOf(
                Analytics.EXTENSION,
                Identity.EXTENSION,
                Target.EXTENSION
            )
            MobileCore.registerExtensions(
                extensions
            ) {
                print("Successfully registered Adobe extensions")
            }

            Identity.getExperienceCloudId {
                mid = it
            }
            MobileCore.configureWithAppID("c6329b3d-6d30-d71e-42a7-e31dc8a71900")
            MobileCore.lifecycleStart(null)
            TargetManager.prefetchContent()
        } catch (e: Exception) {
            print("Exception = ${e.message}")
        }

    }

}