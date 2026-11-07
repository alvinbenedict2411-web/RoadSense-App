package com.example.beastmode_5.App.Presentation.Functions

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.core.content.ContextCompat
import java.io.File

fun ImageCapture(context: Context,imagecapture: ImageCapture,onImagesaved:(String)-> Unit){
    println("ImageCapture CALLED")
    val contentvalues= ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME,
            "Image ${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH,"Pictures/CameraX-Image")
    }
    val contentresolver=context.contentResolver
    val directory=File(context.filesDir,"Potholes")
    if(!directory.exists()){
        directory.mkdirs()
    }
    val file= File(
        directory,
        "Pothole ${System.currentTimeMillis()}.jpg"
    )
    val ouputfile= ImageCapture.OutputFileOptions.Builder(file).build()

    imagecapture.takePicture(
        ouputfile,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
               onImagesaved(file.absolutePath)
               println("✅ IMAGE SAVED")
            }
            override fun onError(exception: ImageCaptureException) {
                println("❌ IMAGE CAPTURE ERROR = ${exception.message}")
            }
        }
    )
}