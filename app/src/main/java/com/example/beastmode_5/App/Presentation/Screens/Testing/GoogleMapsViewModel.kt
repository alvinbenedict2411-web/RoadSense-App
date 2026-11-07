package com.example.beastmode_5.App.Presentation.Screens.Testing

import android.Manifest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class GoogleMapsViewModel: ViewModel() {

    var haspermission by mutableStateOf(false)
        private set

    fun changepermission(value:Boolean){
        haspermission=value
    }

    var permissionstoask=arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
        private set
}