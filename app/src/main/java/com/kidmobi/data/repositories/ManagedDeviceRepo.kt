package com.kidmobi.data.repositories

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kidmobi.business.utils.enums.DbCollection
import com.kidmobi.business.utils.extensions.toManagedDevice
import com.kidmobi.data.model.ManagedDevice
import com.kidmobi.data.model.MobileDevice
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ManagedDeviceRepo @Inject constructor(
    db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    var mobileDeviceRepo: MobileDeviceRepo
) {
    private val collection = db.collection(DbCollection.ManagedDevices.name)

    suspend fun removeAllDevices() {
        auth.currentUser?.let { user ->
            collection.document(user.uid).delete().await()
        }
    }

    suspend fun getByCurrentUserId(): ManagedDevice {
        var device = ManagedDevice()
        auth.currentUser?.let { user ->
            device = collection.document(user.uid).get().await().toManagedDevice()
        }
        return device
    }

    suspend fun updateCurrentUserDeviceList(device: ManagedDevice) {
        auth.currentUser?.let { user ->
            update(user.uid, device)
        }
    }

    suspend fun getListOfUserDevices(): MutableList<MobileDevice> {
        val list = mutableListOf<MobileDevice>()
        val umd = getByCurrentUserId()
        for (deviceId in umd.devices) {
            val device = mobileDeviceRepo.getById(deviceId)
            list.add(device)
        }
        return list
    }

    private suspend fun update(documentId: String, entity: ManagedDevice) {
        collection.document(documentId).set(entity).await()
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

    suspend fun add(entity: ManagedDevice) {
        auth.currentUser?.let { user ->
            collection.document(user.uid).set(entity).await()
        }
    }
}