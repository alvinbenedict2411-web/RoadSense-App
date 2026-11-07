package com.example.beastmode_5.App.Presentation.Viewmodels

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.beastmode_5.App.Data.FireBase.FireBaseDataClass.PotholeFireBase
import com.example.beastmode_5.App.Data.FireBase.Firebase.FireBase
import com.example.beastmode_5.App.Data.Reposistory.RepositoryImplementation
import com.example.beastmode_5.App.Data.RoomDataBase.Pothole
import com.example.beastmode_5.App.Data.RoomDataBase.PotholeDataBase
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch

class GoogleMapsScreenViewModel(application: Application): AndroidViewModel(application) {

    private val firebase = FireBase(application)

    private val dao = PotholeDataBase.getdatabase(application).Pothole_Dao()

    private val repository = RepositoryImplementation(dao,firebase)

    var allpotholedfromcloud by mutableStateOf<List<PotholeFireBase>>(emptyList())
        private set

    var potholedetails by mutableStateOf<List<Pothole>>(emptyList())
        private set

    var UnifiedPotholeDetals by mutableStateOf<List<Pothole>>(emptyList())
        private set

    var latestlocationforcameraposition by mutableStateOf<Location?>(null)
        private set

    var permissionsasked = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )
        private set

    var nearestpothole by mutableStateOf<Pothole?>(null)
        private set

    fun updatenearstpothole(pothole: Pothole) {
        nearestpothole = pothole
    }

    var nearstpotholedistance by mutableStateOf<Float>(0f)
        private set

    var direction by mutableStateOf<String>("")
        private set

    var visitedpotholeids = mutableListOf<Int>()
        private set

    fun setvisitedpotholestoempty() {
        visitedpotholeids.clear()
    }

    fun updatedirection(direction: String) {
        this.direction = direction
    }

    fun getdetails() {
        viewModelScope.launch {
            repository.getpotholedetailsFlow().collect { results ->
                potholedetails = results

            }
        }
    }

    init {
        getdetails()
        GetCloudUpdates()
    }

    fun ispermissiongranted(): Boolean {
        return permissionsasked.all {
            ContextCompat.checkSelfPermission(
                application,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private val Locationrequest =
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L).build()


    private val LocationCallBack = object : LocationCallback() {
        override fun onLocationResult(locationresult: LocationResult) {
            latestlocationforcameraposition = locationresult.lastLocation
        }
    }

    private val fusedloactionclient = LocationServices.getFusedLocationProviderClient(application)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun getlocationupdatesstarted() {
        if (!ispermissiongranted()) {
            Toast.makeText(application, "permission not granted", Toast.LENGTH_SHORT).show()
            return
        }
        fusedloactionclient.requestLocationUpdates(
            Locationrequest,
            LocationCallBack,
            Looper.getMainLooper()
        )
    }

    fun getlocation(): Location? {
        return latestlocationforcameraposition
    }

    fun stoplocationupdades() {
        fusedloactionclient.removeLocationUpdates(LocationCallBack)
    }

    fun nearestpothole(lat: Double, long: Double): Pothole? {
        if (UnifiedPotholeDetals.isNotEmpty() && UnifiedPotholeDetals.all { it.id in visitedpotholeids }) {
            setvisitedpotholestoempty()
        }
        var nearestpothole: Pothole? = null
        var naerstdistance = Float.MAX_VALUE //defining a large number to start off
        val output = FloatArray(1)
        for (pothole in UnifiedPotholeDetals) {
            if (pothole.id in visitedpotholeids) {
                continue
            }
            Location.distanceBetween(
                lat,
                long,
                pothole.latitude,
                pothole.longitude,
                output
            )
            val meters = output[0]
            if (meters < naerstdistance) {
                naerstdistance = meters
                nearestpothole = pothole
            }
        }
        nearstpotholedistance = naerstdistance
        return nearestpothole
    }

    fun calculatedirection(cameralat: Double, cameralong: Double, nearstpothole: Pothole?): Float {
        var bearing = 0f
        nearstpothole?.let {
            val cameraLocation = Location("").apply {
                latitude = cameralat
                longitude = cameralong
            }
            val potholelocation = Location("").apply {
                latitude = nearstpothole.latitude
                longitude = nearstpothole.longitude
            }
            bearing = cameraLocation.bearingTo(potholelocation)
        }
        return bearing
    }

    fun getdirection(bearing: Float): String {
        val normalizedBearing = (bearing + 360) % 360
        val direction = when {
            normalizedBearing >= 337.5 || normalizedBearing < 22.5 -> "North"
            normalizedBearing < 67.5 -> "Northeast"
            normalizedBearing < 112.5 -> "East"
            normalizedBearing < 157.5 -> "Southeast"
            normalizedBearing < 202.5 -> "South"
            normalizedBearing < 247.5 -> "Southwest"
            normalizedBearing < 292.5 -> "West"
            else -> "Northwest"
        }
        return direction
    }

    fun checkIfPotholeReached(cameraLat: Double, cameraLong: Double) {
        nearestpothole?.let { pothole ->
            val distance = nearstpotholedistance

            if (distance <= 5f) {
                visitedpotholeids.add(pothole.id)
            }
        }
    }

    fun GetCloudUpdates(){
        viewModelScope.launch {
            repository.StartLiveUpdatedCloud(GotUpdatedPotholes = {lists->
                allpotholedfromcloud=lists
            },
                onError = {value->
                    Toast.makeText(application,"Failed to load cloud data due to ${value}",Toast.LENGTH_SHORT).show()
                })
        }
    }

    fun UpdateUnifiedPotholeDetails(potholes: List<Pothole>){
        UnifiedPotholeDetals=potholes
    }
}
