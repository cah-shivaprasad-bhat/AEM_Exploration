package com.cardinalhealth.vantus.adobe

import android.app.Application
import android.content.Context
import android.util.Log
import com.adobe.marketing.mobile.Analytics
import com.adobe.marketing.mobile.Assurance
import com.adobe.marketing.mobile.Identity
import com.adobe.marketing.mobile.LoggingMode
import com.adobe.marketing.mobile.MobileCore
import com.adobe.marketing.mobile.Target
import com.cardinalhealth.vantus.App
import javax.inject.Inject

class AdobeManager @Inject constructor(
    val context: Context
) {
    fun init() {
        MobileCore.setApplication(context.applicationContext as Application)
        MobileCore.setLogLevel(LoggingMode.VERBOSE)
        try {
            MobileCore.initialize(context.applicationContext as Application, "42925fc841db/9da9ec012a62/launch-0f1f5a8ce051-development") {
                MobileCore.lifecycleStart(null)
                TargetManager.prefetchContent()
            }

        } catch (e: Exception) {
            Log.d("ADOBE_MANAGER", "Exception = ${e.message}")

        }

    }

}