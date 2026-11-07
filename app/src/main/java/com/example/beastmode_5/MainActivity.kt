package com.example.beastmode_5

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.beastmode_5.App.Presentation.Screens.Permissions.CameraPermissions
import com.example.beastmode_5.App.Presentation.Screens.StaticImg.StaticImgTest
import com.example.beastmode_5.App.Presentation.Screens.Testing.GoogleMapsTest
import com.example.beastmode_5.App.Presentation.SplashScreen.RoadSenseSplashHost
import com.example.beastmode_5.ui.theme.BeastMode_5Theme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @androidx.annotation.RequiresPermission(allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION])
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BeastMode_5Theme {
                RoadSenseSplashHost {
                    CameraPermissions(application)
                }
            }
        }
    }
}
