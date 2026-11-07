package com.example.beastmode_5.App.Presentation.Screens.GoogleMapsScreen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.beastmode_5.App.Presentation.Viewmodels.GoogleMapsScreenViewModel
import com.example.beastmode_5.R
import com.example.beastmode_5.ui.theme.AccentBlue
import com.example.beastmode_5.ui.theme.DarkBackground
import com.example.beastmode_5.ui.theme.LargeColor
import com.example.beastmode_5.ui.theme.MediumColor
import com.example.beastmode_5.ui.theme.SmallColor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Locale.getDefault
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.beastmode_5.App.Data.RoomDataBase.Pothole
import com.example.beastmode_5.App.Presentation.Navigation.BottomNavigationBar
import com.example.beastmode_5.ui.theme.HomePrimaryText
import com.example.beastmode_5.ui.theme.HomeSecondaryText
import com.example.beastmode_5.ui.theme.HomeSurfaceColor
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.maps.android.compose.rememberMarkerState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.NonCancellable.key

@SuppressLint("SuspiciousIndentation")
@androidx.annotation.RequiresPermission(allOf = [android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION])
@Composable
fun GoogleMapsScreen(SinglePothole: LatLng?=null,
    viewmodel: GoogleMapsScreenViewModel,navcontroller: NavController) {

    val context = LocalContext.current

    val cameraposition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(12.9719, 77.5937),
            10f
        )
    }
    LaunchedEffect(SinglePothole) {
        SinglePothole?.let {
            cameraposition.position = CameraPosition.fromLatLngZoom(
                SinglePothole,
                16f
            )
        }
    }

    LaunchedEffect(
        viewmodel.potholedetails,
        viewmodel.allpotholedfromcloud
    ) {
        val potholesfromcloud=mutableListOf<Pothole>()
        // Convert active Firebase potholes to Room Pothole objects.
        val potholesfromcloudEligible = viewmodel.allpotholedfromcloud
            .filter { it.status != "Resolved" }
            potholesfromcloudEligible.forEach { pothole->
          val potholetoadd=  Pothole(
              id = pothole.id,
              latitude = pothole.latitude,
              longitude = pothole.longitude,
              area_name = pothole.area_name,
              city_name = pothole.city_name,
              confidence = pothole.confidence,
              pothole_size = pothole.pothole_size,
              image_path = pothole.image_path,
              timestamp = pothole.timestamp,
              firebasekey = pothole.firebasekey
            )
                potholesfromcloud.add(potholetoadd)
        }

        // Keep Room potholes that don't already exist in Firebase.
        val uniqueRoomPotholes=viewmodel.potholedetails.filter { roompothole->
            val alreadyExistsInFireBase=potholesfromcloud.any { cloudpothole->
                val keyexists=!roompothole.firebasekey.isNullOrBlank() && roompothole.firebasekey==cloudpothole.firebasekey
                val latlongsmatch=roompothole.latitude==cloudpothole.latitude && roompothole.longitude==cloudpothole.longitude
                keyexists || latlongsmatch
            }
            !alreadyExistsInFireBase
        }

        // Combine unique Room potholes with active Firebase potholes.
        viewmodel.UpdateUnifiedPotholeDetails(
            uniqueRoomPotholes + potholesfromcloud
        )
        // we combine bcz the potholescloud contains synced one and room contains synced and non synced maybe so we filter out all synced one and keep only non synced one and that is stored in room and then we combine
    }

    LaunchedEffect(Unit) {
        viewmodel.getlocationupdatesstarted()
        println("got locations")
    }

    LaunchedEffect(cameraposition.position.target) {
        if(viewmodel.potholedetails.isNotEmpty()){
            println("potholes not empty and it got here ")
          val pothole= viewmodel.nearestpothole(
                cameraposition.position.target.latitude,
                cameraposition.position.target.longitude
            )
            pothole?.let {it->
                println("Successfully got the nearest pothole")
                viewmodel.updatenearstpothole(it)
                println("updated nearest pothole")
                viewmodel.checkIfPotholeReached(cameraposition.position.target.latitude,cameraposition.position.target.longitude)
                println("checked if pothole reached")
               val bearing= viewmodel.calculatedirection(cameraposition.position.target.latitude,cameraposition.position.target.longitude,viewmodel.nearestpothole)
                println("calculated direction")
               val direction=viewmodel.getdirection(bearing)
                println("got direction")
                viewmodel.updatedirection(direction)
                println("updated direction")
            }

        }
    }

    //runs when screen closes
    DisposableEffect(Unit) {
        onDispose {
            viewmodel.stoplocationupdades()
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar("Maps",navcontroller)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val location = viewmodel.getlocation()
                    if (location == null) {
                        Toast.makeText(
                            context,
                            "Getting Location Please Wait For Few Seconds And Try Again...",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                    location?.let {
                        cameraposition.position = CameraPosition.fromLatLngZoom(
                            LatLng(location.latitude, location.longitude),
                            16f,
                        )
                    }
                },
                modifier = Modifier.padding(bottom = 16.dp, end = 16.dp),
                containerColor = AccentBlue,
                contentColor = DarkBackground,
            ) {
                Icon(
                    Icons.Default.LocationSearching,
                    contentDescription = "Current Location"
                )
            }
        }
    ) { innerpadding ->

        Box(modifier = Modifier.padding(innerpadding)) {
            Box(modifier = Modifier.fillMaxSize()) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraposition,
                    properties = MapProperties(
                        isMyLocationEnabled = viewmodel.ispermissiongranted(),
                        isTrafficEnabled = true,
                        isBuildingEnabled = true,
                        isIndoorEnabled = true,
                        mapType = MapType.NORMAL,
                        maxZoomPreference = 20f,
                        minZoomPreference = 3f,
                    ),
                    uiSettings = MapUiSettings(
                        zoomControlsEnabled = false,
                        myLocationButtonEnabled = false,
                        compassEnabled = true,
                        mapToolbarEnabled = false,
                        rotationGesturesEnabled = true,
                        scrollGesturesEnabled = true,
                        scrollGesturesEnabledDuringRotateOrZoom = true,
                        tiltGesturesEnabled = true,
                        zoomGesturesEnabled = true,
                    )

                ) {
                    val smallPotholeIcon = remember(context) {
                        drawableToBitmapDescriptor(
                            context,
                            R.drawable.pothole_small
                        )
                    }

                    val mediumPotholeIcon = remember(context) {
                        drawableToBitmapDescriptor(
                            context,
                            R.drawable.pothole_medium
                        )
                    }

                    val largePotholeIcon = remember(context) {
                        drawableToBitmapDescriptor(
                            context,
                            R.drawable.pothole_large
                        )
                    }
                    val formater = SimpleDateFormat(
                        "dd MMM yyyy, hh:mm a",
                        Locale.getDefault()
                    )
                    viewmodel.UnifiedPotholeDetals.forEach { potholedetail ->
                        key(potholedetail.firebasekey, potholedetail.id,
                            potholedetail.latitude, potholedetail.longitude, potholedetail.timestamp){

                            val size =
                                potholedetail.pothole_size.lowercase(Locale.getDefault())
                            val timestamp = formater.format(Date(potholedetail.timestamp))
                            val markerstate= rememberUpdatedMarkerState(
                                position = LatLng(
                                    potholedetail.latitude,
                                    potholedetail.longitude
                                )
                            )
                            MarkerInfoWindow(
                                state = markerstate,
                                title = "Pothole",
                                snippet = "${potholedetail.pothole_size} pothole",
                                icon = when {
                                    size == "small" -> smallPotholeIcon
                                    size == "medium" -> mediumPotholeIcon
                                    else -> largePotholeIcon
                                },
                                onClick = {
                                    cameraposition.position = CameraPosition.fromLatLngZoom(
                                        LatLng(potholedetail.latitude, potholedetail.longitude),
                                        16f
                                    )
                                    false
                                }
                            ) {
                                Card(shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White.copy(0.7f)
                                    )){
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.Start
                                    ) {
                                        Text(
                                            text = "POTHOLE DETECTED",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = HomePrimaryText
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Row {
                                            Text(
                                                text = "Size",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                modifier = Modifier.width(85.dp),
                                                color= HomeSecondaryText
                                            )

                                            Text(
                                                text = potholedetail.pothole_size,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = when {
                                                    size == "small" -> SmallColor
                                                    size == "medium" -> MediumColor
                                                    else -> LargeColor
                                                }
                                            )
                                        }

                                        Row {
                                            Text(

                                                text = "Confidence",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.width(85.dp),
                                                color = HomeSecondaryText
                                            )
                                            Spacer(modifier = Modifier.width(2.dp))
                                            Text(
                                                text = String.format(
                                                    Locale.getDefault(),
                                                    "%.1f%%",
                                                    potholedetail.confidence * 100
                                                ),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = when {
                                                    potholedetail.confidence >= 0.80 -> SmallColor
                                                    potholedetail.confidence >= 0.50 -> MediumColor
                                                    else -> LargeColor
                                                }
                                            )
                                        }
                                        Row {
                                            Text(
                                                text = "Area",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                modifier = Modifier.width(85.dp),
                                                color=HomeSecondaryText
                                            )
                                            Text(
                                                text = potholedetail.area_name,
                                                fontSize = 14.sp,
                                                color = Color.Black
                                            )
                                        }
                                        Row {
                                            Text(
                                                text = "City",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.width(85.dp),
                                                color=HomeSecondaryText
                                            )
                                            Text(
                                                text = potholedetail.city_name,
                                                fontSize = 14.sp,
                                                color=Color.Black
                                            )
                                        }
                                        Row {
                                            Text(
                                                text = "Detected",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                modifier = Modifier.width(85.dp),
                                                color=HomeSecondaryText
                                            )

                                            Text(
                                                text = timestamp,
                                                fontSize = 13.sp,
                                                color=Color.Black
                                            )
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
                if (viewmodel.potholedetails.isNotEmpty()){
                    Card(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .safeContentPadding()
                            .padding(top = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = HomeSurfaceColor
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 24.dp,
                                vertical = 10.dp
                            ),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = when (viewmodel.direction) {
                                    "North" -> "↑"
                                    "Northeast" -> "↗"
                                    "East" -> "→"
                                    "Southeast" -> "↘"
                                    "South" -> "↓"
                                    "Southwest" -> "↙"
                                    "West" -> "←"
                                    "Northwest" -> "↖"
                                    else -> "↑"
                                },
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentBlue
                            )
                            Text(
                                text = "${viewmodel.direction} • ${viewmodel.nearstpotholedistance.toInt()} m",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentBlue
                            )
                        }
                    }
            }
                else{
                    Card(modifier = Modifier
                        .align(Alignment.TopCenter)
                        .safeContentPadding()
                        .padding(top = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = HomeSurfaceColor
                        )) {
                        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally){
                            Text(text = "No Potholes Found",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = AccentBlue)
                        }
                    }
                }
                }
            }
        }
    }
private fun drawableToBitmapDescriptor(
    context: android.content.Context,
    drawableId: Int
): BitmapDescriptor {

    val drawable = ContextCompat.getDrawable(context, drawableId)
        ?: return BitmapDescriptorFactory.defaultMarker()

    val width = if (drawable.intrinsicWidth > 0) {
        drawable.intrinsicWidth
    } else {
        96
    }

    val height = if (drawable.intrinsicHeight > 0) {
        drawable.intrinsicHeight
    } else {
        96
    }

    val bitmap = Bitmap.createBitmap(
        width,
        height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)

    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bitmap)
}

//Launched effect meaning Run this when I first enter the screen, and run it again whenever KEY changes.