package com.example.beastmode_5.App.Data.RoomDataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName ="PotholeDetails")
data class Pothole(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val latitude: Double,
    val longitude: Double,
    val area_name: String,
    val city_name:String,
    val confidence:Double,
    val pothole_size:String,
    val image_path:String?,
    val timestamp:Long=System.currentTimeMillis(),
    val firebasekey:String?=null
)
