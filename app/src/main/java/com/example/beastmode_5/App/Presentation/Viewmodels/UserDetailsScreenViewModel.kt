package com.example.beastmode_5.App.Presentation.Viewmodels

import android.app.Application
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import com.example.beastmode_5.App.Data.FireBase.FireBaseDataClass.PotholeFireBase
import com.example.beastmode_5.App.Data.FireBase.Firebase.FireBase
import com.example.beastmode_5.App.Data.Reposistory.RepositoryImplementation
import com.example.beastmode_5.App.Data.RoomDataBase.PotholeDataBase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.launch

class UserDetailsScreenViewModel(application: Application): AndroidViewModel(application) {

    private val dao= PotholeDataBase.getdatabase(application).Pothole_Dao()

    private val firebase= FireBase(application)

    private val repository= RepositoryImplementation(dao,firebase)

    private val auth= FirebaseAuth.getInstance()

    var islaoding by mutableStateOf(false)
        private set

    var errormsg by mutableStateOf<String?>(null)
        private set

    var allpotholesfromroomCount by mutableStateOf<Int>(0)
        private set

    var allareasfromroomCount by mutableStateOf<Int>(0)
        private set

    var allareasfromcloud by mutableStateOf<Int>(0)
        private set

    var totalreportsinroom by mutableStateOf<Int>(0)
        private set

    var allpotholesfromcloudCount by mutableStateOf<Int>(0)
        private set

    var WaitingtoSync by mutableStateOf<Int>(0)
        private set

    var CloudStatus by mutableStateOf(false)
        private set

    var username by mutableStateOf<String>("")
        private set

    var useremail by mutableStateOf<String>("")
        private set

    var userIcon by mutableStateOf<Uri?>(null)
        private set

    var usernameforchange by mutableStateOf<String>("")
        private set

    var showalertdialogforusersignout by mutableStateOf(false)
        private set

    var showbottomsheetforusernamechange by mutableStateOf(false)
        private set

    fun getlivedataroom(){
        viewModelScope.launch{
            repository.getpotholedetailsFlow().collect { potholes->
                val result=potholes
                allpotholesfromroomCount=result.size
                allareasfromroomCount=result.map { it.area_name }.distinct().size
                totalreportsinroom=result.size
                WaitingtoSync=result.count { it.firebasekey==null }
            }
        }
    }

    fun getcloudlivedatastarted(){
        islaoding=true
        viewModelScope.launch {
            try {
                repository.StartLiveUpdatedCloud(GotUpdatedPotholes = {potholes->
                    val result=potholes
                    allareasfromcloud=result.map { it.area_name }.distinct().size
                    allpotholesfromcloudCount=result.size
                    CloudStatus=true
                    islaoding=false
                }, onError = {value->
                    islaoding=false
                    CloudStatus=false
                    Toast.makeText(application,"Failed to get cloud data due to $value", Toast.LENGTH_SHORT).show()
                })
            }
            catch (e: Exception){
                islaoding=false
                Toast.makeText(application,"Failed to get cloud data due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    init{
        getuserdata()
        getlivedataroom()
        getcloudlivedatastarted()
    }

    fun SignOutUser(){
        islaoding=true
        auth.signOut()
        islaoding=false
        Toast.makeText(application,"Signed out successfully", Toast.LENGTH_SHORT).show()
    }

    fun getuserdata(){
        username=auth.currentUser?.displayName?:"RoadSense User"
        usernameforchange=username
        useremail=auth.currentUser?.email?:"RoadSenseUserEmail@gmail.com"
        userIcon=auth.currentUser?.photoUrl
    }

    fun UpdateUsername(NewName:String){
        val name=NewName.trim()
        if (name.isBlank()) {
            Toast.makeText(application, "Username cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        val user=auth.currentUser
        if(user==null){
            Toast.makeText(application,"User not found", Toast.LENGTH_SHORT).show()
            return
        }
        val profileupdates= userProfileChangeRequest {
            displayName=name
        }
        islaoding=true
        user.updateProfile(profileupdates).addOnCompleteListener { task->
            if(task.isSuccessful){
                islaoding=false
                username=name
                Toast.makeText(application,"Username updated successfully", Toast.LENGTH_SHORT).show()
            }
            else{
                islaoding=false
                Toast.makeText(application,"Failed to update username", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun UpdateUserNameForChange(name: String){
        usernameforchange=name
    }

    fun UpdateUserNameforchangeonEditClickButton(){
        usernameforchange=username
    }

    fun updateshowbottomsheetupdatesetfalse(){
        showbottomsheetforusernamechange=false
    }

    fun updateshowbottomsheetupdatesettrue(){
        showbottomsheetforusernamechange=true
    }

    fun updateshowalertdialogupdatesetfalse(){
        showalertdialogforusersignout=false
    }

    fun updateshowalertdialogupdatesettrue(){
        showalertdialogforusersignout=true
    }

    fun reloaduserdata(){
        val user=auth.currentUser
        if(user==null){
            Toast.makeText(application,"User not found", Toast.LENGTH_SHORT).show()
            return
        }
        islaoding=true
        user.reload().addOnCompleteListener { task->
            if(task.isSuccessful){
                islaoding=false
                Toast.makeText(application,"User data reloaded successfully", Toast.LENGTH_SHORT).show()
                getuserdata()
            }
            else{
                islaoding=false
                Toast.makeText(application,"Failed to reload user data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}