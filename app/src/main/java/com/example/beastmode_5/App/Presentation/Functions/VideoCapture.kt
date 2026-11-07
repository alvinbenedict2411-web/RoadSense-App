package com.example.beastmode_5.App.Presentation.Functions

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors

@RequiresPermission(Manifest.permission.RECORD_AUDIO)
fun VideoCapture(context: Context, recorder: Recorder,videocapture: VideoCapture<Recorder>): Recording{

    val contentValues= ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "Video ${System.currentTimeMillis()}.mp4")
        put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
        put(MediaStore.MediaColumns.RELATIVE_PATH, "Movies/CameraX-Video")
    }

    val contentresolver= context.contentResolver

    val mediastoreoutput= MediaStoreOutputOptions.Builder(contentresolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).setContentValues(contentValues).build()

    val Pendingrecording=videocapture.output
        .prepareRecording(
            context,
            mediastoreoutput
        )
        .withAudioEnabled()

    return Pendingrecording.start(ContextCompat.getMainExecutor(context),{ event ->
        when(event){
            is VideoRecordEvent.Start->{
                Toast.makeText(context, "Recording started", Toast.LENGTH_SHORT).show()
            }
        }
    })
}