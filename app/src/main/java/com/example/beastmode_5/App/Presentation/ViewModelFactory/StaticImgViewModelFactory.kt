package com.example.beastmode_5.App.Presentation.ViewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beastmode_5.App.Presentation.Viewmodels.StaticImgViewmodel
import kotlin.jvm.java

class StaticImgViewModelFactory(val application: Application): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StaticImgViewmodel::class.java)){
            return StaticImgViewmodel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
        }
    }