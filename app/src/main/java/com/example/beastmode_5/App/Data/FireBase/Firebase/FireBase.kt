package com.example.beastmode_5.App.Data.FireBase.Firebase

import android.app.Application
import android.net.Uri
import android.widget.Toast
import com.example.beastmode_5.App.Data.FireBase.FireBaseDataClass.PotholeFireBase
import com.example.beastmode_5.App.Data.RoomDataBase.Pothole
import com.example.beastmode_5.App.Data.RoomDataBase.PotholeDao
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class FireBase(val application: Application) {

    private val auth = FirebaseAuth.getInstance()

    private val database = FirebaseDatabase.getInstance()

    private val storage = FirebaseStorage.getInstance()

    private val storageRef = storage.reference

    fun addpotholes(
        potholes: List<Pothole>,
        onFinished: (Boolean) -> Unit,
        onUpload: (Pothole, String) -> Unit
    ) {
        var totalToUpload = 0 // these 2 are used for showing toast at crt time
        var completedUploads = 0
        val useruid = auth.currentUser?.uid
        if (useruid == null) {
            Toast.makeText(
                application,
                "Failed to Sync Data User Not Logged In",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val ref = database.getReference("users/${useruid}/potholes")
        ref.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val existingpotholesInCloud = mutableListOf<PotholeFireBase>()
                    for (childSnapshot in snapshot.children) {
                        val pothole = childSnapshot.getValue(PotholeFireBase::class.java)
                        pothole?.let {
                            existingpotholesInCloud.add(it)
                        }
                    }

                   val activePotholesInCloud= existingpotholesInCloud.filter {
                        it.status!="Resolved"
                    }

                    val potholestoupload = potholes.filter { pothole ->
                        val hasnokey = pothole.firebasekey == null
                        val isduplicate = activePotholesInCloud.any { existingPothole ->
                            calculateDistance(
                                pothole.latitude,
                                pothole.longitude,
                                existingPothole.latitude,
                                existingPothole.longitude
                            ) <= 5.0
                        }
                        hasnokey && !isduplicate
                    }
                    totalToUpload = potholestoupload.size

                    if (totalToUpload == 0) {
                        onFinished(true)
                        Toast.makeText(application, "No New Data To Sync", Toast.LENGTH_SHORT)
                            .show()
                        return
                    }

                    potholes.forEach { pothole -> // can use potholestoupload instead of potholes i used potholes bcz i had coded before only so its ok
                        val hasNoFireBaseKey = pothole.firebasekey == null
                        val isDuplicate = activePotholesInCloud.any { existingPothole ->
                            calculateDistance(
                                pothole.latitude,
                                pothole.longitude,
                                existingPothole.latitude,
                                existingPothole.longitude
                            ) <= 5.0
                        }
                        if (hasNoFireBaseKey && !isDuplicate) {
                            val potholeuniquekey = ref.push()
                            val imgpath = pothole.image_path
                            val file = File(imgpath)
                            val uri = Uri.fromFile(file)
                            val imgref = storageRef.child("Pothole Images").child("${useruid}")
                                    .child(potholeuniquekey.key!!)
                            imgref.putFile(uri).addOnSuccessListener {
                                imgref.downloadUrl.addOnSuccessListener { url ->
                                    val imgpath = url.toString()
                                    val potholefire = PotholeFireBase(
                                        id = pothole.id,
                                        firebasekey = potholeuniquekey.key!!,
                                        latitude = pothole.latitude,
                                        longitude = pothole.longitude,
                                        area_name = pothole.area_name,
                                        city_name = pothole.city_name,
                                        confidence = pothole.confidence,
                                        pothole_size = pothole.pothole_size,
                                        image_path = imgpath,
                                    )
                                    potholeuniquekey.setValue(potholefire)
                                        .addOnSuccessListener {
                                            println("Success inserting pothole data with id ${pothole.id}")
                                            completedUploads++
                                            onUpload(pothole, potholeuniquekey.key!!)
                                            if (completedUploads == totalToUpload) {
                                                Toast.makeText(
                                                    application,
                                                    "Data Synced Successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                onFinished(true)
                                            }
                                        }.addOnFailureListener {
                                            Toast.makeText(
                                                application,
                                                "Failed to Sync Data",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            onFinished(true)
                                        }
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        application,
                                        "Failed to Sync Data Due To Not able to get image",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onFinished(true)
                                }
                            }.addOnFailureListener {
                                Toast.makeText(
                                    application,
                                    "Failed to Sync Data",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onFinished(true)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(application, "Failed to Sync Data", Toast.LENGTH_SHORT).show()
                    onFinished(true)
                }
            }
        )
    }

    fun LiveData(UpdatedPotholeLists: (List<PotholeFireBase>) -> Unit, onError: (String?) -> Unit) {
        val currentuser = auth.currentUser?.uid
        if (currentuser == null) {
            Toast.makeText(
                application,
                "Failed to Load Data User Not Logged In",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("users/${auth.currentUser?.uid}/potholes")
        ref.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val allpotholesFromCloud = mutableListOf<PotholeFireBase>()
                    for (eachsnapshot in snapshot.children) {
                        val pothole = eachsnapshot.getValue(PotholeFireBase::class.java)
                        pothole?.let {
                            allpotholesFromCloud.add(it)
                        }
                    }
                    UpdatedPotholeLists(allpotholesFromCloud)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(application, "Failed to Load Data", Toast.LENGTH_SHORT).show()
                    onError(error.message)
                }
            }
        )
    }
}

private fun calculateDistance(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Double {
    val earthRadius = 6371000.0

    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)

    val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) *
            Math.cos(Math.toRadians(lat2)) *
            Math.sin(dLon / 2) * Math.sin(dLon / 2)

    val c = 2 * Math.atan2(
        Math.sqrt(a),
        Math.sqrt(1 - a)
    )

    return earthRadius * c
}
/**
 * Calculates the distance between two geographical coordinates
 * (latitude and longitude) using the Haversine formula.
 *
 * The Haversine formula accounts for the Earth's curvature
 * when calculating the distance between two points.
 *
 * @param lat1 Latitude of the first location.
 * @param lon1 Longitude of the first location.
 * @param lat2 Latitude of the second location.
 * @param lon2 Longitude of the second location.
 *
 * @return Distance between the two locations in metres.
 *
 * This distance is used to check whether a newly detected
 * pothole is within 5 metres of an existing pothole in Firebase.
 * If the distance is <= 5 metres, the new detection is treated
 * as a potential duplicate and is not uploaded.
 */
