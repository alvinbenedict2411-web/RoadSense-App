package com.example.beastmode_5.App.Data.Reposistory

import com.example.beastmode_5.App.Data.FireBase.FireBaseDataClass.PotholeFireBase
import com.example.beastmode_5.App.Data.FireBase.Firebase.FireBase
import com.example.beastmode_5.App.Data.RoomDataBase.Pothole
import com.example.beastmode_5.App.Data.RoomDataBase.PotholeDao
import com.example.beastmode_5.App.Domain.Repositrory.Repository
import kotlinx.coroutines.flow.Flow

class RepositoryImplementation(private val dao: PotholeDao,
    private val firebase: FireBase): Repository {

    override suspend fun insertpothole(pothole: Pothole) {
        dao.InsertPothole(pothole)
    }

    override suspend fun updatepothole(pothole: Pothole) {
        dao.UpdatePothole(pothole)
    }

    override suspend fun deletepothole(pothole: Pothole) {
        dao.DeletePothole(pothole)
    }

    override suspend fun deletebyid(id: Int) {
        dao.deletebyid(id)
    }

    override suspend fun getbyid(id: Int): Pothole {
      return dao.getbyid(id)
    }

    override  fun getpotholedetailsFlow(): Flow<List<Pothole>> {
        return dao.Getdetails()
    }

    override suspend fun getpotholedetails(): List<Pothole> {
        return dao.getalldetails()
    }

    override suspend fun InsertAllPotholesCloud(potholes: List<Pothole>, onFinished: (Boolean) -> Unit, onUpload: (Pothole, String) -> Unit) {
        firebase.addpotholes(potholes, onFinished = {value->
            onFinished(value)
        },
            onUpload = {pothole,value->
                onUpload(pothole,value)
            })
    }

    override suspend fun StartLiveUpdatedCloud(GotUpdatedPotholes: (List<PotholeFireBase>)->Unit,onError:(String?)->Unit) {
        firebase.LiveData(UpdatedPotholeLists = {list->
            GotUpdatedPotholes(list)
        },
            onError = {value->
                onError(value)
            })
    }
}