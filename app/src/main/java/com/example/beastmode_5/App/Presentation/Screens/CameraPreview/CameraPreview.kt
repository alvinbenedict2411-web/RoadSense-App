package com.example.beastmode_5.App.Presentation.Screens.CameraPreview

import android.Manifest
import android.graphics.RectF
import android.os.Build
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.beastmode_5.App.Data.RoomDataBase.Pothole
import com.example.beastmode_5.App.Presentation.Classes.DetectionBox
import com.example.beastmode_5.App.Presentation.Classes.ModelRun
import com.example.beastmode_5.App.Presentation.Functions.ImageCapture
import com.example.beastmode_5.App.Presentation.Viewmodels.CameraPreviewViewmodel
import com.example.beastmode_5.App.Presentation.Viewmodels.PotholeViewModel
import java.util.concurrent.Executors

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraPreview(viewmodel: CameraPreviewViewmodel= viewModel(),
                  potholeviewmodel: PotholeViewModel,navController: NavController) {
    LaunchedEffect(Unit)  {
        potholeviewmodel.startLocationUpdates()
        println("LOCATION UPDATES STARTED")
    }

    val context=LocalContext.current

    val lifecycle_owner=LocalLifecycleOwner.current

    val preview_view= remember{
        PreviewView(context)
    }

    val Image_Analysis=remember {
        ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
    }
    val executor= remember {
        Executors.newSingleThreadExecutor()
    }

    val model= remember {
        ModelRun(context)
    }

    Scaffold() {innerpadding->
        Box(modifier = Modifier.fillMaxSize()
            .padding(innerpadding)) {

            AndroidView(
                factory = {
                    preview_view.apply {
                        scaleType=PreviewView.ScaleType.FILL_CENTER
                    }
                }
            )
            IconButton(onClick = {
                viewmodel.ChangeCamera()
            },
                modifier = Modifier.align(Alignment.TopEnd)
                    .safeContentPadding()) {
                Icon(Icons.Default.Cameraswitch,
                    contentDescription = "Camera",
                    tint=Color.White)
            }

            IconButton(onClick = {
                navController.popBackStack()
            },
                modifier = Modifier.align(Alignment.TopStart)
                    .safeContentPadding()) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint=Color.White)
            }

            if(viewmodel.result.isNotEmpty()){
                Canvas(modifier = Modifier.fillMaxSize()) {

                    viewmodel.result.forEach { pothole->
                        drawRect(
                            color=Color.Red,
                            topLeft = Offset(pothole.box.left,pothole.box.top),
                            size = Size(
                                pothole.box.width(),
                                pothole.box.height()
                            ),
                            style = Stroke(
                                width = 5f
                            )
                        )
                    }
                }
            }

            val camera_provider_future= remember {
                ProcessCameraProvider.getInstance(context)
            }
            Image_Analysis.setAnalyzer(executor, ImageAnalysis.Analyzer{imageProxy ->
                val img=imageProxy.image
                val temp=mutableListOf<DetectionBox>()
                if(img!=null) {
                    val bitmap = imageProxy.toBitmap()
                    val image_width =
                        if (imageProxy.imageInfo.rotationDegrees == 90 || imageProxy.imageInfo.rotationDegrees == 270) imageProxy.height else imageProxy.width
                    val image_height =
                        if (imageProxy.imageInfo.rotationDegrees == 90 || imageProxy.imageInfo.rotationDegrees == 270) imageProxy.width else imageProxy.height
                    println("Img Width ${imageProxy.width}")
                    println("Img Height ${imageProxy.height}")
                    println("Rotataion ${imageProxy.imageInfo.rotationDegrees}")
                    println("Preview Width${preview_view.width}")
                    println("Preview Height${preview_view.height}")
                    val result =
                        model.RunLiveDetection(bitmap, imageProxy.imageInfo.rotationDegrees)
                    println("YOLO RESULT SIZE = ${result.size}")
                    result.forEach { pothole ->
                        val scale = maxOf(
                            preview_view.width / image_width.toFloat(),
                            preview_view.height / image_height.toFloat()
                        )
                        val scale_width = image_width * scale
                        val scale_height=image_height*scale
                        val horizontaloffset = (scale_width - preview_view.width)/2f
                        val verticaloffset=(scale_height-preview_view.height)/2f
                        println("Scale $scale")
                        println("Scale Width $scale_width")
                        println("Horizontal Offset $horizontaloffset")
                        val box = RectF(
                            pothole.box.left * scale - horizontaloffset,
                            pothole.box.top * scale-verticaloffset,
                            pothole.box.right * scale - horizontaloffset,
                            pothole.box.bottom * scale-verticaloffset
                        )
                        //this is done because to handle the camera rotation to draw crt bounding boxes on front camera
                        val finalbox=if(viewmodel.camera_selected== CameraSelector.DEFAULT_FRONT_CAMERA){
                            RectF(
                                preview_view.width-box.right,
                                box.top,
                                preview_view.width-box.left,
                                box.bottom
                            )
                        }else{
                            box
                        }
                        val Detectionbox = DetectionBox(finalbox, pothole.score)
                        temp.add(Detectionbox)
                    }
                    viewmodel.updateresult(temp)
                    if(result.isNotEmpty() && potholeviewmodel.startProcessing()){
                            val allpotholesinaframe=mutableListOf<Pothole>()
                            val location = potholeviewmodel.getlatestlocation()
                            println("📍 LATEST LOCATION = $location")
                            if (location != null) {
                                val img=imageProxy.toBitmap()
                                val imgpath=potholeviewmodel.getImgPath(context,img,imageProxy.imageInfo.rotationDegrees)
                                println("📸 IMAGE SAVED = $imgpath")
                                potholeviewmodel.getareaname(
                                    location.latitude,
                                    location.longitude,
                                    result = { areaname,city_name ->
                                        result.forEach { potholedetails ->
                                            //when you get the areaname then only do the next part
                                            val size = potholeviewmodel.getpotholesize(
                                                potholedetails.box.width(),
                                                potholedetails.box.height()
                                            )
                                            val newpothole = Pothole(
                                                latitude = location.latitude,
                                                longitude = location.longitude,
                                                area_name = areaname,
                                                city_name = city_name,
                                                pothole_size = size,
                                                confidence = potholedetails.score.toDouble(),
                                                image_path = imgpath
                                            )
                                            allpotholesinaframe.add(newpothole)
                                            println("YOLO RESULT SIZE = ${result.size}")
                                            println("LATEST LOCATION = $location")
                                        }
                                        potholeviewmodel.checkandsave(
                                            location.latitude,
                                            location.longitude,
                                            allpotholesinaframe
                                        )
                                        println("CHECK AND SAVE CALLED")
                                    }
                                )
                            }
                        else if(location==null){
                            potholeviewmodel.stopProcessing()
                            println("📍 LOCATION IS NULL Retrying to get location")
                        }
                    }
                    imageProxy.close()
                    }
                else{
                    viewmodel.setempty()
                    imageProxy.close()
                }
            })

            DisposableEffect(lifecycle_owner,viewmodel.camera_selected){

                camera_provider_future.addListener({

                    val camera_provider=camera_provider_future.get()

                    camera_provider.unbindAll()

                    viewmodel.preview.surfaceProvider=preview_view.surfaceProvider

                    camera_provider.bindToLifecycle(
                        lifecycle_owner,
                        viewmodel.camera_selected,
                        viewmodel.preview,
                        viewmodel.imagecapture,
                        viewmodel.videocapture,
                        Image_Analysis
                        )

                }, ContextCompat.getMainExecutor(context))
            onDispose {
                camera_provider_future.get().unbindAll()
                potholeviewmodel.stoploactionupdates()
            }}
        }
    }
}
//removed ImageCapture bcz it clicks img after the frame is gone i real life so used the img that is processed by model itself to store in img of pothole and its imgpath
