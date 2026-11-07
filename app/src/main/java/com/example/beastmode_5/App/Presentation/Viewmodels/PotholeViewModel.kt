package com.example.beastmode_5.App.Presentation.Viewmodels

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Matrix
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.beastmode_5.App.Data.FireBase.Firebase.FireBase
import com.example.beastmode_5.App.Data.Reposistory.RepositoryImplementation
import com.example.beastmode_5.App.Data.RoomDataBase.Pothole
import com.example.beastmode_5.App.Data.RoomDataBase.PotholeDataBase
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.play.core.aipacks.internal.bi
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale

class PotholeViewModel(application: Application): AndroidViewModel(application) {
    private val dao = PotholeDataBase.getdatabase(application).Pothole_Dao()

    private val firebase = FireBase(application)

    private val repository = RepositoryImplementation(dao,firebase)

    var error_msg by mutableStateOf<String?>(null)
        private set

    var allpotholes by mutableStateOf<List<Pothole>?>(null)
        private set

    var permissionstorequest = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )
        private set

    private var latestloaction by mutableStateOf<LatLng?>(null)


    private val loactionrequest = com.google.android.gms.location.LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        1000L
    ).build()

    private val fusedloactionclient = LocationServices.getFusedLocationProviderClient(application)

    private var isProcessing by mutableStateOf(false)

    fun startProcessing(): Boolean {
        if (isProcessing) {
            return false
        }
        isProcessing = true
        return true
    }

    fun stopProcessing() {
        isProcessing = false
    }

    private val locationcallback = object : LocationCallback() {
        override fun onLocationResult(loactionresult: LocationResult) {
            // updates to new loaction
            val location = loactionresult.lastLocation
            if (location != null) {
                println("📍 NEW LOCATION = $location")
                latestloaction = LatLng(
                    location.latitude,
                    location.longitude
                )
            }
        }
    }

    fun isgranted(): Boolean {
        return permissionstorequest.all {
            ContextCompat.checkSelfPermission(
                application,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun startLocationUpdates() {
        if (!isgranted()){
            println("📍 LOCATION PERMISSION NOT GRANTED")
            return
        }

        fusedloactionclient.requestLocationUpdates(
            loactionrequest,
            locationcallback,
            Looper.getMainLooper()
        )
        println("📍 LOCATION PERMISSION GRANTED")
    }

    fun stoploactionupdates() {
        fusedloactionclient.removeLocationUpdates(locationcallback)
    }

    fun getlatestlocation(): LatLng? {
        return latestloaction
    }

    fun showfailuretoast(potholeSize: String) {
        val toast=Toast.makeText(
            application,
            "${potholeSize} Sized pothole already exists within 200 meters, so it was not saved",
            Toast.LENGTH_SHORT
        )
        toast.show()
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            toast.cancel()
        },1000)
    }

    fun Success_toast(){
        val toast=Toast.makeText(
            application,
            "Pothole Saved",
            Toast.LENGTH_SHORT
        )
        toast.show()
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            toast.cancel()
        },1000)
    }

    fun checkandsave(newlatitude: Double, newlongitude: Double, newpotholelist: List<Pothole>) {
        println("CHECKANDSAVE STARTED")
        viewModelScope.launch {
            try {
                newpotholelist.forEach { newpothole ->
                val result = repository.getpotholedetails()
                println("ROOM RESULTS = ${result.size}")
                var nearbyfound = false
                var smallpotholedetected = false
                var mediumpotholedetected = false
                var largepotholedetected = false
                result.forEach { pothole ->
                    println("EXISTING POTHOLE = $pothole")
                    val distance = FloatArray(1)
                    Location.distanceBetween(
                        newlatitude,
                        newlongitude,
                        pothole.latitude,
                        pothole.longitude,
                        distance
                    )
                    val meters = distance[0]
                    if (meters <= 200f) {
                        nearbyfound = true
                        if (pothole.pothole_size == "Small") {
                            smallpotholedetected = true
                        } else if (pothole.pothole_size == "Medium") {
                            mediumpotholedetected = true
                        } else if (pothole.pothole_size == "Large") {
                            largepotholedetected = true
                        }
                    }
                }
                if (!nearbyfound) {
                    repository.insertpothole(newpothole)
                    Toast.makeText(application, "Pothole Saved", Toast.LENGTH_SHORT).show()
                }
                if (nearbyfound) {
                    var inserted = false
                    if (!smallpotholedetected && newpothole.pothole_size == "Small") {
                        repository.insertpothole(newpothole)
                        inserted = true
                        Toast.makeText(application, "Pothole Saved", Toast.LENGTH_SHORT).show()
                    } else if (!mediumpotholedetected && newpothole.pothole_size == "Medium") {
                        repository.insertpothole(newpothole)
                        inserted = true
                        Toast.makeText(application, "Pothole Saved", Toast.LENGTH_SHORT).show()
                    } else if (!largepotholedetected && newpothole.pothole_size == "Large") {
                        repository.insertpothole(newpothole)
                        inserted = true
                        Toast.makeText(application, "Pothole Saved", Toast.LENGTH_SHORT).show()
                    }
                    if (!inserted) {
                        showfailuretoast(newpothole.pothole_size)
                    }
                }
            }
        }
            catch (e: Exception) {
                error_msg = e.message
            }
            finally {
                stopProcessing()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getareaname(latitude: Double, longitude: Double, result: (String,String) -> Unit) {
        val geocoder = Geocoder(application, Locale.getDefault())
        val address = geocoder.getFromLocation(
            latitude,
            longitude,
            1,
            object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: List<Address?>) {
                    println("GEOCODER SUCCESS")
                    val address = addresses?.firstOrNull()
                    val areaname = address?.subLocality ?:"Unknown Area"
                    val cityname=address?.locality?:"Unknown City"
                    println("AREA NAME = $areaname")
                    result(areaname,cityname)
                }

                override fun onError(errorMessage: String?) {
                    result("Unknown Area","Unknown City")
                }
            }
        )
    }

    fun getpotholesize(width: Float, height: Float): String {
        val area = width * height
        val size = when {
            area < 10000 -> "Small"
            area < 30000 -> "Medium"
            else -> "Large"
        }
        return size
    }

    fun getImgPath(context: Context, img: Bitmap, rotationDegrees: Int): String {
        val rotatedbitmap=rotatebitmap(img,rotationDegrees)
        val directory = File(context.filesDir, "Potholes")
        if(!directory.exists()){
            directory.mkdirs()
        }
        val file=File(directory,"Pothole ${System.currentTimeMillis()}.jpg")
        file.outputStream().use{output->
            rotatedbitmap.compress(Bitmap.CompressFormat.JPEG,100,output)
        }
        return file.absolutePath
    }

    fun rotatebitmap(bitmap: Bitmap,degrees: Int): Bitmap{
        if(degrees==0){
            return bitmap
        }
        val matrix= android.graphics.Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            matrix,
            true
        )
    }

}
