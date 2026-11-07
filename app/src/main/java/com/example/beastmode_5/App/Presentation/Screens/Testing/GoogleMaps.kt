package com.example.beastmode_5.App.Presentation.Screens.Testing

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.icu.text.Transliterator
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.beastmode_5.R
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.JointType
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.RoundCap
import com.google.android.gms.maps.model.SquareCap
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerInfoWindowContent
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polygon
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleMapsTest(viewModel: GoogleMapsViewModel=viewModel()) {

    val context=LocalContext.current
    var location by remember { mutableStateOf<LatLng?>(null) }

    fun haspermission(): Boolean{
        return viewModel.permissionstoask.all {
            ContextCompat.checkSelfPermission(
                context,
                it
            )==PackageManager.PERMISSION_GRANTED
        }
    }
    val permissionlauncher=rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()){permission->
        viewModel.changepermission(haspermission())
    }

    val AmericaLocation= LatLng(37.4219999,-122.0862462)
    val cameraposition= rememberCameraPositionState {
        position= CameraPosition.fromLatLngZoom(AmericaLocation,10f)
    }
    val fusedloacatioclient= LocationServices.getFusedLocationProviderClient(context)
    val loacationrequest= LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,2000L).build()
    val locationcallback=object: LocationCallback(){
        @SuppressLint("SuspiciousIndentation")
        override fun onLocationResult(loactionresult: LocationResult) {
         val locationlocal=loactionresult.lastLocation
            if(locationlocal!=null) {
                location = LatLng(
                    locationlocal.latitude,
                    locationlocal.longitude
                )
            }
        /*    if(location!=null){
                val userloacation= LatLng(
                    location.latitude,
                    location.longitude
                )
                cameraposition.position= CameraPosition.fromLatLngZoom(
                    userloacation,
                    15f
                )
            }    // If we put camera movement here,
    // camera can move whenever a new location update arrives.*/
        }
    }
    if(viewModel.haspermission) {
        DisposableEffect(Unit) {
            fusedloacatioclient.requestLocationUpdates(
                loacationrequest,
                locationcallback,
                Looper.getMainLooper()
            )
            onDispose {
                fusedloacatioclient.removeLocationUpdates(locationcallback)
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if(viewModel.haspermission){
                    Toast.makeText(context,"Repositioning",Toast.LENGTH_SHORT).show()
//this is usefull when on click only you want the camera to move to latest data location we get latest ocation on click bcz we used onloacationcallback exectues when new loc arrives and inside that onloactionresult runs as it is executed on new location recived
// When the button is clicked, get the latest known location
// using lastLocation and move the camera to that location.
// This is different from LocationCallback, which executes
// whenever a new location update is delivered.
                    fusedloacatioclient.lastLocation.addOnSuccessListener { location->
                        if(location!=null){
                            val userloacation= LatLng(
                                location.latitude,
                                location.longitude
                            )
                            cameraposition.position= CameraPosition.fromLatLngZoom(
                                userloacation,
                                15f
                            )
                        }
                    }.addOnFailureListener { error->
                        Toast.makeText(context,"Failed to get location",Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    permissionlauncher.launch(
                        viewModel.permissionstoask
                    )
                }
            },
                modifier = Modifier.padding(bottom = 70.dp,end = 10.dp)) {
                Icon(
                    imageVector = Icons.Default.LocationSearching,
                    contentDescription = "Location"
                )

            }
        }
    ) {innerpadding->
        Box(modifier = Modifier.fillMaxSize()
            .padding(innerpadding)) {

            GoogleMap(modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraposition,
                properties = MapProperties(
                    maxZoomPreference = 20f,
                    minZoomPreference = 3f,
                    isTrafficEnabled = true,
                    mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
                        context,
                        R.raw.maps_style
                    ),
                    isBuildingEnabled = true,
                    isMyLocationEnabled = viewModel.haspermission // this is only used to display blue icon on the map on the user location it doesnt move to the cameraposition to latest loacation automatically
                )
            ) {
                location?.let {
                    Circle(
                        center = location!!,
                        radius = 500.00,
                        clickable = false,
                        fillColor = Color.Red.copy(alpha = 0.2f),
                        strokeColor = Color.Red,
                        strokeWidth = 1f,
                        visible = true,
                        zIndex = 1f,
                        )
                    println("looaction:${location}")
                }
                Polyline(
                    points = listOf(
                        LatLng(37.4219983, -122.0840000), // A
                        LatLng(37.4225000, -122.0835000), // B
                        LatLng(37.4230000, -122.0842000), // C
                        LatLng(37.4227000, -122.0850000), // D
                        LatLng(37.4219000, -122.0855000)  // E
                    ),
                    color = Color.Blue,
                    width = 5f,
                    clickable = false,
                    visible = true,
                    zIndex = 3f,
                    startCap = RoundCap(),
                    endCap = SquareCap(),
                    jointType = JointType.ROUND,
                    pattern = listOf(
                        Dash(10f),
                        Dot()
                    )
                )

                val holes1=listOf(
                    LatLng(37.4220, -122.0848),
                    LatLng(37.4220, -122.0842),
                    LatLng(37.4226, -122.0842),
                    LatLng(37.4226, -122.0848)
                )

                val holes2=listOf(
                    LatLng(37.4230, -122.0848),
                    LatLng(37.4230, -122.0842),
                    LatLng(37.4236, -122.0842),
                    LatLng(37.4236, -122.0848)
                )
                 /* val items=listOf<MarkerItems>(
                    MarkerItems(
                        LatLng(37.4215, -122.0855),
                        "pothole 1",
                        "Pothole detected here"
                    ),
                    MarkerItems(
                        LatLng(37.4216, -122.0854),
                        "pothole 2",
                        "Pothole detected here"
                    ),
                    MarkerItems(
                        LatLng(37.4217, -122.0856),
                        "pothole 3",
                        "Pothole detected here"
                    ),
                    MarkerItems(
                        LatLng(37.4218, -122.0855),
                        "pothole 4",
                        "Pothole detected here"
                    )
                )

                  */

                Polygon(
                    points=listOf(
                        LatLng(37.4215, -122.0855),
                        LatLng(37.4215, -122.0825),
                        LatLng(37.4245, -122.0825),
                        LatLng(37.4245, -122.0855)
                    ),
                    clickable = false,
                    fillColor = Color.Green.copy(alpha = 0.2f),
                    strokeColor = Color.Green,
                    strokeWidth = 1f,
                    visible = true,
                    zIndex = 4f,
                    holes = listOf(holes1,holes2),
                    strokePattern = listOf(
                        Dash(10f),
                        Dot()
                    ),
                    strokeJointType = JointType.ROUND
                )
           /*     items.forEach { item->
                    //dont use markerinfowindowcontent the box is not coming for this version so use normal marker
                MarkerInfoWindowContent(
                    state = MarkerState(position = item.location),
                    title = item.name,
                    snippet = item.description,
                    zIndex = 5f,
                    draggable = false,
                    visible = true,
                    flat = true,
                    onClick = {
                        cameraposition.position= CameraPosition.fromLatLngZoom(
                            item.location,
                            17f
                        )
                        false
                    }
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(3.dp)) {

                        Text(text = item.name,
                            color = Color.Black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold)
                        Text(text = item.description,
                            color = Color.Black,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal)
                        item.img?.let {
                            Icon(painter = painterResource(item.img),
                                contentDescription = "img",
                                modifier = Modifier.fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp)))
                        }
                    }
                }
            } */

                //Clustering
             /*   Clustering(
                    items = items,
                    onClusterClick = {cluster->
                        cameraposition.position= CameraPosition.fromLatLngZoom(
                            cluster.position,
                            17f
                        )
                        false
                    },
                )

              */
            }
            }
        }
}


 /*data class MarkerItems(
        val location:LatLng,
    val name:String,
    val description:String,
    val img:Int?=null,
    val zindex:Float=0f
        ): ClusterItem{
    override fun getPosition(): LatLng {
        return location
    }

    override fun getSnippet(): String? {
        return description
    }

    override fun getTitle(): String? {
        return name
    }

    override fun getZIndex(): Float? {
        return zindex
    }


}

  */

