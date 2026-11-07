package com.example.beastmode_5.App.Presentation.Viewmodels

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.VideoCapture
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.beastmode_5.App.Presentation.Classes.DetectionBox

class CameraPreviewViewmodel: ViewModel() {

    var camera_selected by mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA)
        private set

    fun ChangeCamera(){
        camera_selected=if(camera_selected==CameraSelector.DEFAULT_BACK_CAMERA){
            CameraSelector.DEFAULT_FRONT_CAMERA
        }else{
            CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    var preview= Preview.Builder().build()
        private set

    var imagecapture= ImageCapture.Builder().build()
        private set

    var QualitySetting= QualitySelector.fromOrderedList(
        listOf(Quality.FHD, Quality.HD, Quality.SD),
        FallbackStrategy.lowerQualityOrHigherThan(Quality.SD)
    )
        private set

    var recorder= Recorder.Builder().setQualitySelector(QualitySetting).build()
        private set

    var videocapture= VideoCapture.withOutput(recorder)
        private set

    var result by mutableStateOf<List<DetectionBox>>(emptyList())
        private set

    fun updateresult(result:List<DetectionBox>){
        this.result=result
    }

    fun setempty(){
        result=emptyList()
    }
}