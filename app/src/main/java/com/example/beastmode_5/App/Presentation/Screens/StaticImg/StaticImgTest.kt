package com.example.beastmode_5.App.Presentation.Screens.StaticImg

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.beastmode_5.App.Data.RoomDataBase.Pothole
import com.example.beastmode_5.App.Presentation.Classes.DetectionBox
import com.example.beastmode_5.App.Presentation.Classes.ModelRun
import com.example.beastmode_5.App.Presentation.Viewmodels.StaticImgViewmodel
import com.example.beastmode_5.R
import com.example.beastmode_5.ui.theme.AccentBlue
import com.example.beastmode_5.ui.theme.CardBackground
import com.example.beastmode_5.ui.theme.DarkBackground
import com.example.beastmode_5.ui.theme.HomeBackgroundColor
import com.example.beastmode_5.ui.theme.HomePrimaryText
import com.example.beastmode_5.ui.theme.HomeSecondaryText
import com.example.beastmode_5.ui.theme.HomeSurfaceColor
import com.example.beastmode_5.ui.theme.LargeColor
import com.example.beastmode_5.ui.theme.MediumColor
import com.example.beastmode_5.ui.theme.PrimaryText
import com.example.beastmode_5.ui.theme.SecondaryCardColor
import com.example.beastmode_5.ui.theme.SecondaryText
import com.example.beastmode_5.ui.theme.SmallColor
import com.example.beastmode_5.ui.theme.StaticImgBackgroundColor
import com.example.beastmode_5.ui.theme.StaticImgDisabledButtonColor
import com.example.beastmode_5.ui.theme.StaticImgDisabledTextColor
import com.example.beastmode_5.ui.theme.StaticImgSecondaryText
import com.example.beastmode_5.ui.theme.StaticImgSurfaceColor
import java.io.File

@SuppressLint("LocalContextResourcesRead")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@androidx.annotation.RequiresPermission(allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION])
@Composable
fun StaticImgTest(viewmodel: StaticImgViewmodel=viewModel(),navcontroller: NavController) {
    val context = LocalContext.current
    val bitmap = BitmapFactory.decodeResource(context.resources,R.drawable.imagepicker2)
    println("width ${bitmap.width} height ${bitmap.height}")
    val imagelauncher= rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ){img->
        img?.let {
            viewmodel.updateselecteduri(img)
        }
    }

    LaunchedEffect(Unit)  {
        viewmodel.startrecivingupdates()
    }

    // detectionmodelruncount makes the effect run for every Run Model click,
// even when the number of detected potholes is the same.

    LaunchedEffect(viewmodel.detectionmodelruncount){ //used this counter rather bcz when clicked run model
        if(viewmodel.alldetectedpotholes.isNotEmpty()) {
            val location = viewmodel.getlatestlocation()
            if(location==null){
                Toast.makeText(context,"Location not available yet. Please make sure location is turned on, wait a moment, and try again.",Toast.LENGTH_SHORT).show()
            }
            location?.let {
                val latitute = it.latitude
                val longitute = it.longitude
                viewmodel.getareaname(
                    latitute, longitute,
                    onResult = { areaname, cityname ->
                        viewmodel.alldetectedpotholes.forEach { potholebox ->
                            val size = viewmodel.potholesize(
                                potholebox.box.width(),
                                potholebox.box.height()
                            )
                            val pothole = Pothole(
                                latitude = latitute,
                                longitude = longitute,
                                area_name = areaname,
                                city_name = cityname,
                                confidence = potholebox.score.toDouble(),
                                pothole_size = size,
                                image_path = viewmodel.imgpath
                            )
                            viewmodel.checkandsave(pothole)
                        }
                    })
            }
        }
    }

    LaunchedEffect(viewmodel.selecteduri){
        viewmodel.selecteduri?.let {
         /*   val inputstream=context.contentResolver.openInputStream(it)
            val bitmap=BitmapFactory.decodeStream(inputstream)  can do this or do this*/
            val imgbitmap=context.contentResolver.openInputStream(it)?.use{input->
                BitmapFactory.decodeStream(input)
            }
            if(imgbitmap!=null) {
                viewmodel.updategotImage(imgbitmap)
            }
            val path=viewmodel.ImgPath(context)
            viewmodel.updateImagPath(path)
        }

    }
    val model=remember {
        ModelRun(context)
    }
    Scaffold(
        topBar = {
            TopBarStaticImgScreen(onClicked = {
                navcontroller.popBackStack()
            })
        }
    ) { innerpadding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .background(StaticImgBackgroundColor)
                .padding(innerpadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 10.dp),
            ) {
                item {
                    Text(
                        text = "Pothole Detection",
                        color = PrimaryText,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Upload an image to detect potholes",
                        color = StaticImgSecondaryText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth()
                            .aspectRatio(
                                if (viewmodel.gotImage != null) viewmodel.gotImage!!.width.toFloat() / viewmodel.gotImage!!.height.toFloat() else
                                    bitmap.width.toFloat() / bitmap.height.toFloat()
                            ),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = StaticImgSurfaceColor
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (viewmodel.selecteduri == null) {
                                Image(
                                    painter = painterResource(R.drawable.imagepicker2),
                                    contentDescription = "Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            if (viewmodel.selecteduri != null) {
                                AsyncImage(
                                    model = viewmodel.selecteduri,
                                    contentDescription = "Image",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }

                            if (viewmodel.alldetectedpotholes.isNotEmpty()) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    viewmodel.alldetectedpotholes.forEach { boxvalue ->
                                        val scalex =
                                            size.width / viewmodel.gotImage!!.width.toFloat()
                                        val scaley =
                                            size.height / viewmodel.gotImage!!.height.toFloat()
                                        val left = boxvalue.box.left * scalex
                                        val right = boxvalue.box.right * scalex
                                        val top = boxvalue.box.top * scaley
                                        val bottom = boxvalue.box.bottom * scaley
                                        val width = right - left
                                        val height = bottom - top
                                        drawRect(
                                            color = Color.Red,
                                            style = Stroke(width = 2.dp.toPx()),
                                            topLeft = Offset(
                                                x = left,
                                                y = top
                                            ),
                                            size = Size(
                                                width = width,
                                                height = height
                                            ),
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))

                    Button(
                        onClick = {
                            imagelauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentBlue,
                            contentColor = Color.White,
                            disabledContainerColor = SecondaryCardColor,
                            disabledContentColor = SecondaryText
                        )
                    ) {
                        Text(
                            text = "Select Image",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            val boxes = model.runstaticimg(viewmodel.gotImage!!)
                            viewmodel.updatePotholes(boxes)
                            if (boxes.isEmpty()) {
                                Toast.makeText(context, "No Potholes Detected", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            viewmodel.Count()
                        },
                        modifier = Modifier.fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentBlue,
                            contentColor = Color.White,
                            disabledContainerColor = StaticImgDisabledButtonColor,
                            disabledContentColor = StaticImgDisabledTextColor
                        ),
                        enabled = viewmodel.gotImage != null
                    ) {
                        Text(
                            text = "Run Model",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(18.dp))

                    if (viewmodel.alldetectedpotholes.isNotEmpty()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = StaticImgSurfaceColor
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            )
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(18.dp)
                            ) {
                                Text(
                                    text = "Detection Result",
                                    color = HomePrimaryText,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "${viewmodel.alldetectedpotholes.size} Potholes Detected",
                                    color = StaticImgSecondaryText,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(18.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = viewmodel.smallcount.toString(),
                                            color = SmallColor,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Small",
                                            color = StaticImgSecondaryText,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = viewmodel.mediumcount.toString(),
                                            color = MediumColor,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Medium",
                                            color = StaticImgSecondaryText,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = viewmodel.largecount.toString(),
                                            color = LargeColor,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))

                                        Text(
                                            text = "Large",
                                            color = StaticImgSecondaryText,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    if (
                        viewmodel.latestlocation != null ||
                        viewmodel.areanamefordisplay != null ||
                        viewmodel.citynamefordisplay != null
                    ) {

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = StaticImgSurfaceColor
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            )
                        ) {

                            Column(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(16.dp)
                            ) {

                                Text(
                                    text = "Detection Information",
                                    color = HomePrimaryText,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(14.dp))

                                viewmodel.latestlocation?.let {

                                    Text(
                                        text = "Location",
                                        color = StaticImgSecondaryText,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )

                                    Spacer(modifier = Modifier.height(1.dp))

                                    Text(
                                        text = "${it.latitude}, ${it.longitude}",
                                        color = HomePrimaryText,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }

                                viewmodel.areanamefordisplay?.let {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "Area",
                                        color = StaticImgSecondaryText,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(1.dp))
                                    Text(
                                        text = it,
                                        color = HomePrimaryText,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                                viewmodel.citynamefordisplay?.let {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "City",
                                        color = StaticImgSecondaryText,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(1.dp))
                                    Text(
                                        text = it,
                                        color = HomePrimaryText,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            viewmodel.stoplocationupdates()
        }
    }
}