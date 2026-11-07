package com.example.beastmode_5.App.Presentation.Viewmodels

import android.app.Application
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.beastmode_5.App.Data.FireBase.Firebase.FireBase
import com.example.beastmode_5.App.Data.Reposistory.RepositoryImplementation
import com.example.beastmode_5.App.Data.RoomDataBase.Pothole
import com.example.beastmode_5.App.Data.RoomDataBase.PotholeDataBase
import com.example.beastmode_5.App.Domain.Repositrory.Repository
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
class HomeScreenViewModel(application: Application): AndroidViewModel(application) {

    private val dao = PotholeDataBase.getdatabase(application).Pothole_Dao()

    private val firebase = FireBase(application)

    private val repository = RepositoryImplementation(dao, firebase)

    private val auth= FirebaseAuth.getInstance()

    var recentupto3potholedetails by mutableStateOf<List<Pothole>>(emptyList())
        private set

    var totalpotholes by mutableStateOf<Int>(0)
        private set

    var totalareas by mutableStateOf<Int>(0)
        private set

    var potholesdetectedtoday by mutableStateOf<Int>(0)
        private set

    var lastdetectedareaname by mutableStateOf<String>("")
        private set

    var lastdetectedcityname by mutableStateOf<String>("")
        private set

    var lastpotholedetectedtime by mutableStateOf<Long>(0L)
        private set

    var allpotholes by mutableStateOf<List<Pothole>>(emptyList())
        private set

    var IconUrl by mutableStateOf<Uri?>(null)
        private set

    @RequiresApi(Build.VERSION_CODES.O)
    private val today= LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    val todaystart=today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    @RequiresApi(Build.VERSION_CODES.O)
    val starttmr=today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getstarted() {
        viewModelScope.launch {
            repository.getpotholedetailsFlow().collect { results ->
                val sorted=results.sortedBy { it.timestamp }
                    allpotholes = sorted
                potholesdetectedtoday=allpotholes.count {
                    it.timestamp>=todaystart && it.timestamp<starttmr
                }
                    totalpotholes = allpotholes.size
                    totalareas = allpotholes.map { it.area_name }.distinct().size
                    val lastpothole=allpotholes.lastOrNull()
                lastpothole?.let {
                    lastdetectedareaname=lastpothole.area_name
                    lastdetectedcityname=lastpothole.city_name
                    lastpotholedetectedtime=lastpothole.timestamp
                }
                recentupto3potholedetails = allpotholes.takeLast(3)
            }
        }
    }

    init {
        getstarted()
        getuserdata()
    }

    fun getuserdata() {
        val user = auth.currentUser
        IconUrl = user?.photoUrl
    }
}