package com.example.beastmode_5.App.Data.RoomDataBase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PotholeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun InsertPothole(insertpothole:Pothole)

    @Update
    suspend fun UpdatePothole(updatepothole:Pothole)

    @Delete
    suspend fun DeletePothole(deletepothole:Pothole)

    @Query("Select * from PotholeDetails order by timestamp desc")
    fun Getdetails(): Flow<List<Pothole>>

    //delete through id

    @Query("delete from PotholeDetails where id=:id")
    suspend fun deletebyid(id:Int)

    @Query("select * from PotholeDetails where id=:id")
    suspend fun getbyid(id:Int):Pothole

    @Query("select * from PotholeDetails")
    suspend fun getalldetails():List<Pothole>

}