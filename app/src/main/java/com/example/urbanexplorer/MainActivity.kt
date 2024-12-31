package com.example.urbanexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.example.urbanexplorer.ui.UrbanExplorerApp
import com.example.urbanexplorer.ui.theme.UrbanExplorerTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UrbanExplorerTheme(dynamicColor = false) {
                Surface {
                    val windowSize = calculateWindowSizeClass(this)
                    UrbanExplorerApp(windowSize.widthSizeClass)
                }
            }
        }
    }
}

