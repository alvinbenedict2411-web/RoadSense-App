package com.example.beastmode_5.App.Presentation.Viewmodels

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationRequest
import android.net.Uri
import android.os.Build
import android.os.Looper
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.toLowerCase
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.beastmode_5.App.Data.FireBase.Firebase.FireBase
import com.example.beastmode_5.App.Data.Reposistory.RepositoryImplementation
import com.example.beastmode_5.App.Data.RoomDataBase.Pothole
import com.example.beastmode_5.App.Data.RoomDataBase.PotholeDataBase
import com.example.beastmode_5.App.Presentation.Classes.DetectionBox
import com.google.ai.edge.litert.CompiledModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale


class StaticImgViewmodel(application: Application): AndroidViewModel(application) {
    private val dao = PotholeDataBase.getdatabase(application).Pothole_Dao()

    private val firebase = FireBase(application)

    private val repository = RepositoryImplementation(dao,firebase)

    private val permissionstoask=arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    var detectionmodelruncount by mutableStateOf(0)
        private set

    var alldetectedpotholes by mutableStateOf<List<DetectionBox>>(emptyList())
        private set

    var smallcount by mutableStateOf(0)
        private set

    var mediumcount by mutableStateOf(0)
        private set

    var largecount by mutableStateOf(0)
        private set

    fun updatePotholes(potholes: List<DetectionBox>) {
        alldetectedpotholes = potholes
        detectionmodelruncount++
    }

    var areanamefordisplay by mutableStateOf<String?>(null)
        private set

    var citynamefordisplay by mutableStateOf<String?>(null)
    private set

    var latestlocation by mutableStateOf<Location?>(null)
        private set

    var selecteduri by mutableStateOf<Uri?>(null)
        private set

    fun updateselecteduri(uri: Uri) {
        selecteduri = uri
    }

    var gotImage by mutableStateOf<Bitmap?>(null)
        private set

    var imgpath by mutableStateOf<String?>(null)
        private set

   private val locationrequest= com.google.android.gms.location.LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY,1000L).build()

    private val fusedloactionclient= LocationServices.getFusedLocationProviderClient(application)

    private val locationcallback=object : LocationCallback(){
        override fun onLocationResult(loactionresult: LocationResult) {
            loactionresult.lastLocation?.let {
                latestlocation=it
            }
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    fun startrecivingupdates(){
        if(!ispermissiongranted()){
            Toast.makeText(application,"Permission not granted",Toast.LENGTH_SHORT).show()
            return
        }
        fusedloactionclient.requestLocationUpdates(
            locationrequest,
            locationcallback,
            Looper.getMainLooper()
        )
    }

    fun updateImagPath(path: String) {
        imgpath = path
    }

    fun updategotImage(bitmap: Bitmap) {
        gotImage = bitmap
    }

    fun ispermissiongranted(): Boolean{
        return permissionstoask.all {
            ContextCompat.checkSelfPermission(
                application,
                it
            )== PackageManager.PERMISSION_GRANTED
        }
    }

    fun ImgPath(context: Context): String {
        val directory = File(context.filesDir, "Potholes")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, "Pothole ${System.currentTimeMillis()}.jpg")
        selecteduri?.let {
            context.contentResolver.openInputStream(it).use {input->
                file.outputStream().use { output->
                    input?.copyTo(output)
                }
            }
        }
        return file.absolutePath
    }

    fun checkandsave(pothole: Pothole) {
        var isnearby = false
        var isSmallfound = false
        var isLargefound = false
        var isMediumfound = false
        val output = FloatArray(1)
        viewModelScope.launch {
            val allpotholes = repository.getpotholedetails()
            allpotholes.forEach { potholedetail ->
                Location.distanceBetween(
                    pothole.latitude,
                    pothole.longitude,
                    potholedetail.latitude,
                    potholedetail.longitude,
                    output
                )
                val meters = output[0]
                if (meters <= 200) {
                    isnearby = true
                    if (potholedetail.pothole_size.lowercase(Locale.getDefault()) == "small") {
                        isSmallfound = true
                    }
                    if (potholedetail.pothole_size.lowercase(Locale.getDefault()) == "medium") {
                        isMediumfound = true
                    }
                    if (potholedetail.pothole_size.lowercase(Locale.getDefault()) == "large") {
                        isLargefound = true
                    }
                }
            }
            if (!isnearby) {
                repository.insertpothole(pothole)
            }
            else if(isnearby){
                var isinserted=false
                if(!isSmallfound && pothole.pothole_size.lowercase(Locale.getDefault())=="small"){
                    isinserted=true
                    repository.insertpothole(pothole)
                }
                else if(!isMediumfound && pothole.pothole_size.lowercase(Locale.getDefault())=="medium"){
                    isinserted=true
                    repository.insertpothole(pothole)
                }
                else if(!isLargefound && pothole.pothole_size.lowercase(Locale.getDefault())=="large"){
                    isinserted=true
                    repository.insertpothole(pothole)
                }
                if(!isinserted){
                    showfailuretoast(pothole.pothole_size)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun getareaname(latitute: Double,longitute: Double,onResult:(String, String)-> Unit){
        val geocoder= Geocoder(application, Locale.getDefault())
        geocoder.getFromLocation(
            latitute,
            longitute,
            1,

            object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: List<Address?>) {
                    val address=addresses.firstOrNull()
                    val areaname=address?.subLocality?:"Unknown Area"
                    val cityname=address?.locality?:"Unknown City"
                    areanamefordisplay=areaname
                    citynamefordisplay=cityname
                    onResult(areaname,cityname)
                }
            }
        )
    }

    fun potholesize(width: Float, height: Float): String{
        val area=width*height
        val size = when {
            area < 10000 -> "Small"
            area < 30000 -> "Medium"
            else -> "Large"
        }
        return size
    }

    fun showfailuretoast(potholeSize:String) {
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

    fun stoplocationupdates(){
        fusedloactionclient.removeLocationUpdates(locationcallback)
    }

    fun Count(){
        smallcount=0
        mediumcount=0
        largecount=0
        alldetectedpotholes.forEach {
            val size=potholesize(it.box.width(),it.box.height())
            when(size.lowercase(Locale.getDefault())){
                "small"->smallcount++
                "medium"->mediumcount++
                "large"->largecount++
            }
        }
    }

    fun getlatestlocation(): Location? {
        return latestlocation
    }
}