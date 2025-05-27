package com.cardinalhealth.vantus

import android.app.Application
import com.cardinalhealth.vantus.adobe.AdobeManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        AdobeManager(context = baseContext).also {
            it.init()
        }
        super.onCreate()

    }
}