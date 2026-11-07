package com.example.beastmode_5.App.Data.FireBase.FireBaseDataClass

data class PotholeFireBase(
    val id:Int,
    val firebasekey:String,
    val latitude: Double,
    val longitude: Double,
    val area_name: String,
    val city_name:String,
    val confidence:Double,
    val pothole_size:String,
    val image_path:String,
    val timestamp:Long=System.currentTimeMillis(),
    val status:String="Pending"
)
