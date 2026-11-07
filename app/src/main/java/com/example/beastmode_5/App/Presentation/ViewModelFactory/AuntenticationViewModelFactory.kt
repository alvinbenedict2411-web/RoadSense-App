package com.example.beastmode_5.App.Presentation.ViewModelFactory

import android.app.Application
import androidx.compose.runtime.retain.retain
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beastmode_5.App.Presentation.Viewmodels.AuthenticationViewModel
import com.example.beastmode_5.App.Presentation.Viewmodels.GoogleMapsScreenViewModel
import kotlin.jvm.java

class AuntenticationViewModelFactory(val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AuthenticationViewModel::class.java)){
                return AuthenticationViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
    }
}