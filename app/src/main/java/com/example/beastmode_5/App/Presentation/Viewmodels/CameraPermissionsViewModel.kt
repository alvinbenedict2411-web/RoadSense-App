package com.example.beastmode_5.App.Presentation.Viewmodels

import android.Manifest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CameraPermissionsViewModel: ViewModel() {

    var permissions=arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
        private set

    var permissionsGranted by mutableStateOf(false)
        private set

    fun updatePermissionsGranted(value: Boolean){
        permissionsGranted=value
    }

    var showalertdialog by mutableStateOf(false)
        private set

    fun updateShowAlertDialog(value: Boolean){
        showalertdialog=value
    }

}
