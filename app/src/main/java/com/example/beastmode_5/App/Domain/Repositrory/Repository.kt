package com.example.beastmode_5.App.Domain.Repositrory

import com.example.beastmode_5.App.Data.FireBase.FireBaseDataClass.PotholeFireBase
import com.example.beastmode_5.App.Data.RoomDataBase.Pothole
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun getpotholedetailsFlow(): Flow<List<Pothole>>

    suspend fun insertpothole(pothole: Pothole)

    suspend fun deletepothole(pothole: Pothole)

    suspend fun updatepothole(pothole: Pothole)

    suspend fun deletebyid(id: Int)

    suspend fun getbyid(id: Int): Pothole

    suspend fun getpotholedetails():List<Pothole>

    suspend fun InsertAllPotholesCloud(potholes:List<Pothole>,onFinished:(Boolean)->Unit,onUpload:(Pothole, String)->Unit)

    suspend fun StartLiveUpdatedCloud(GotUpdatedPotholes:(List<PotholeFireBase>)->Unit,onError:(String?)->Unit)
}