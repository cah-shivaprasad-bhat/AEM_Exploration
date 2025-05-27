package com.cardinalhealth.vantus.adobe

import android.app.Application
import android.content.Context
import android.util.Log
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
        MobileCore.configureWithAppID("c6329b3d-6d30-d71e-42a7-e31dc8a71900")
        try {
            val extensions = listOf(
                Analytics.EXTENSION,
                Identity.EXTENSION,
                Target.EXTENSION
            )
            MobileCore.registerExtensions(
                extensions
            ) {
                System.out.print("Successfully registered Adobe extensions")
            }

            Identity.getExperienceCloudId {
                mid = it
            }
            MobileCore.lifecycleStart(null)
        } catch (e: Exception) {
            Log.d("ADOBE_MANAGER", "Exception = ${e.message}")

        }

    }

}