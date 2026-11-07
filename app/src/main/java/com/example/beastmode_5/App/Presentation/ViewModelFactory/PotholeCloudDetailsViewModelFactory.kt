package com.example.beastmode_5.App.Presentation.ViewModelFactory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.beastmode_5.App.Presentation.Viewmodels.PotholeCloudDetailsViewModel

class PotholeCloudDetailsViewModelFactory(val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(PotholeCloudDetailsViewModel::class.java)){
            return PotholeCloudDetailsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
        }
}