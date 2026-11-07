package com.example.beastmode_5.App.Data.RoomDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Pothole::class], version = 2)
abstract class PotholeDataBase: RoomDatabase() {
    //connect dao
    abstract fun Pothole_Dao(): PotholeDao

    companion object{
        var INSTANCE: PotholeDataBase?=null

        fun getdatabase(context: Context): PotholeDataBase{
            return INSTANCE?:synchronized(this){
                Room.databaseBuilder(
                    context.applicationContext,
                    PotholeDataBase::class.java,
                    "Pothole_database"
                ).fallbackToDestructiveMigration().build().also {
                    INSTANCE=it
                }
            }
        }
    }
}