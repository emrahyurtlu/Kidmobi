package com.kidmobi.assets.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.assets.enums.DbCollection
import com.kidmobi.assets.utils.printsln
import com.kidmobi.assets.utils.toUserMobileDevice
import com.kidmobi.mvvm.model.MobileDevice
import com.kidmobi.mvvm.model.UserMobileDevice
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserMobileDeviceRepo @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    //private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    //private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collection = db.collection(DbCollection.UserMobileDevices.name)

    @Inject
    lateinit var mobileDeviceRepo: MobileDeviceRepo

    suspend fun removeAllDevices() {
        auth.currentUser?.let { user ->
            collection.document(user.uid).delete().await()
        }
    }

    suspend fun getByCurrentUserId(): UserMobileDevice {
        var device: UserMobileDevice = UserMobileDevice.init()
        auth.currentUser?.let { user ->
            device = collection.document(user.uid).get().await().toUserMobileDevice()
        }
        printsln(device, "UserMobileDeviceRepo::getById()")

        return device
    }

    suspend fun getListOfUserDevices(): MutableList<MobileDevice> {
        val list = mutableListOf<MobileDevice>()
        val umd = getByCurrentUserId()
        for (deviceId in umd.devices) {
            val device = mobileDeviceRepo.getById(deviceId)
            list.add(device)
        }
        printsln(list, "UserMobileDeviceRepo::getListOfUserDevices()")
        return list
    }

    fun update(documentId: String, entity: UserMobileDevice) {
        collection.document(documentId).set(entity)
    }

    suspend fun remove(documentId: String) {
        auth.currentUser?.let { user ->
            val umd = getByCurrentUserId()
            umd.devices.remove(documentId)
            update(entity = umd, documentId = user.uid)
        }
    }

    suspend fun docExists(documentId: String): Boolean {
        val userMobileDevice = collection.document(documentId).get().await()
        return userMobileDevice.exists()
    }

    suspend fun add(entity: UserMobileDevice) {
        auth.currentUser?.let { user ->
            collection.document(user.uid).set(entity).await()
        }
    }
}