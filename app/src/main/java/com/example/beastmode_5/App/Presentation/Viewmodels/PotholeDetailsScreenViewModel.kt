package com.example.beastmode_5.App.Presentation.Viewmodels

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.beastmode_5.App.Data.FireBase.Firebase.FireBase
import com.example.beastmode_5.App.Data.Reposistory.RepositoryImplementation
import com.example.beastmode_5.App.Data.RoomDataBase.Pothole
import com.example.beastmode_5.App.Data.RoomDataBase.PotholeDataBase
import kotlinx.coroutines.launch
import java.io.File

class PotholeDetailsScreenViewModel(application: Application): AndroidViewModel(application) {
    private val dao = PotholeDataBase.getdatabase(application).Pothole_Dao()

    private val firebase = FireBase(application)

    private val repository = RepositoryImplementation(dao,firebase)

    var isloading by mutableStateOf(false)
        private set

    var error_msg by mutableStateOf<String?>(null)
        private set

    var potholedetails by mutableStateOf<List<Pothole>>(emptyList())
        private set

    var isSyncButtonEnabled by mutableStateOf(true)
        private set

    var allresults by mutableStateOf<List<Pothole>>(emptyList())
        private set

    var allareas by mutableStateOf(listOf("All"))
        private set

    var selectedarea by mutableStateOf("All")
        private set

    var showdialog by mutableStateOf(false)
        private set

    fun Changeshowdialog(value: Boolean) {
        showdialog = value
    }

    fun getpotholedetails() {
        viewModelScope.launch {
            try {
                isloading = true
                val directory = File(application.filesDir, "Potholes")
                val imgs = directory.listFiles()
                repository.getpotholedetailsFlow().collect { result ->
                    val results = result
                    allresults = results
                    val categories = results.map { it.area_name }.distinct().sorted()
                    allareas = listOf("All") + categories
                    applyfilters()
                    val imgsused = results.map { it.image_path }.distinct()
                    imgs?.forEach { img ->
                        if (img.absolutePath !in imgsused) {
                            img.delete()
                        }
                    }
                    isloading = false
                }
            } catch (e: Exception) {
                error_msg = e.message
            } finally {
                isloading = false
            }
        }
    }

    fun applyfilters() {
        if (selectedarea == "All") {
            potholedetails = allresults
        } else if (selectedarea !in allareas) {
            selectedarea = "All"
            potholedetails = allresults
        } else {
            potholedetails = allresults.filter { it.area_name == selectedarea }
        }
    }

    init {
        getpotholedetails()
    }

    fun ChangeAreaName(area: String) {
        selectedarea = area
        applyfilters()
    }

    fun deletePothole(id: Int) {
        viewModelScope.launch {
            repository.deletebyid(id)
            Toast.makeText(application, "Pothole Details Deleted", Toast.LENGTH_SHORT).show()
        }

    }

    var idtodelete by mutableStateOf<Int?>(null)
        private set

    var areanamefordialog by mutableStateOf<String?>(null)
        private set

    fun updateid(id: Int?) {
        idtodelete = id
    }

    fun updatearea(areaname: String?) {
        areanamefordialog = areaname
    }

    fun SyncData(){
        viewModelScope.launch {
            try{
                isloading=true
                isSyncButtonEnabled=false
                val allpotholesdetails=repository.getpotholedetails()
                repository.InsertAllPotholesCloud(allpotholesdetails,onFinished = {value->
                    isSyncButtonEnabled=value
                    isloading=false
                },
                    onUpload = {pothole,value->
                        viewModelScope.launch {
                            repository.updatepothole(pothole.copy(firebasekey = value))
                        }
                    })
                Toast.makeText(application,"Please Wait Data is being Synced",Toast.LENGTH_SHORT).show()

            }
            catch(e:Exception){
                Toast.makeText(application,"Sync Failed Please Turn On Internet And Try Again",Toast.LENGTH_SHORT).show()
                isSyncButtonEnabled=true
                isloading=false
            }
        }
    }
}