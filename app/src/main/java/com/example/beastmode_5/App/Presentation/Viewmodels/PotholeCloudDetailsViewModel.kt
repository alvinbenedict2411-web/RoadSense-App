package com.example.beastmode_5.App.Presentation.Viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.beastmode_5.App.Data.FireBase.FireBaseDataClass.PotholeFireBase
import com.example.beastmode_5.App.Data.FireBase.Firebase.FireBase
import com.example.beastmode_5.App.Data.Reposistory.RepositoryImplementation
import com.example.beastmode_5.App.Data.RoomDataBase.PotholeDataBase
import kotlinx.coroutines.launch

class PotholeCloudDetailsViewModel(application: Application): AndroidViewModel(application){

    private val dao= PotholeDataBase.getdatabase(application).Pothole_Dao()

    private val firebase= FireBase(application)

    private val repository= RepositoryImplementation(dao,firebase)

    var islaoding by mutableStateOf(false)
        private set

    var errormsg by mutableStateOf<String?>(null)
        private set

    var allpotholes by mutableStateOf<List<PotholeFireBase>>(emptyList())
        private set

    var allareas by mutableStateOf(listOf("All"))
        private set

    var selectedarea by mutableStateOf("All")
        private set

    var potholedetails by mutableStateOf<List<PotholeFireBase>>(emptyList())
        private set

    fun applyfilters(){
        if(selectedarea=="All"){
            potholedetails=allpotholes
        }
        else{
            potholedetails=allpotholes.filter { it.area_name==selectedarea }
        }
    }

    fun GetLiveDataStarted(){
        islaoding=true
        errormsg=null
        viewModelScope.launch {
            try {
                repository.StartLiveUpdatedCloud(GotUpdatedPotholes = {
                    val result=it.sortedByDescending { it.timestamp }
                    allpotholes=result
                    val areas=result.map { it.area_name }.distinct().sorted()
                    allareas=listOf("All")+areas
                    applyfilters()
                    islaoding=false
                },
                    onError = {
                        islaoding=false
                        errormsg=it
                    })
            }
            catch (e:Exception){
                islaoding=false
                errormsg=e.message
            }
        }
    }

    init {
        GetLiveDataStarted()
    }

    fun ChangeSelectedArea(selectedarea: String){
        this.selectedarea=selectedarea
        applyfilters()
    }

}