package com.cardinalhealth.vantus

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adobe.marketing.mobile.Analytics
import com.adobe.marketing.mobile.MobileCore
import com.cardinalhealth.vantus.adobe.TargetManager
import com.cardinalhealth.vantus.ui.theme.AEM_ExplorationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AEM_ExplorationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        TargetManager.prefetchContent()
        logAnalytics()
        return super.onCreateView(name, context, attrs)
    }

    fun logAnalytics() {
        Log.d("MAIN_ACTIVITY", "Logging Analytics Event")
        val map = hashMapOf<String, String>("name" to "cardinal", "evar1121" to "value1121")
        MobileCore.trackState("TRACK_STATE", map)
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AEM_ExplorationTheme {
        Greeting("Android")
    }
}