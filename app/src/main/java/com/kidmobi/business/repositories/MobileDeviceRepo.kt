package com.kidmobi.business.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kidmobi.business.enums.DbCollection
import com.kidmobi.business.utils.extensions.toMobileDevice
import com.kidmobi.business.utils.extensions.toMobileDeviceList
import com.kidmobi.mvvm.model.MobileDevice
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class MobileDeviceRepo @Inject constructor(
    db: FirebaseFirestore
) : BaseRepo<MobileDevice> {

    private var collection: CollectionReference = db.collection(DbCollection.MobileDevices.name)

    override suspend fun getById(documentId: String): MobileDevice {
        val result = collection.document(documentId).get().await().toMobileDevice()
        Timber.d("$result")
        return result
    }

    override suspend fun getList(): MutableList<MobileDevice> {
        val list = collection.get().await().toMobileDeviceList()
        Timber.d("$list")
        return list
    }

    override suspend fun add(entity: MobileDevice) {
        collection.document(entity.deviceId).set(entity, SetOptions.merge()).await()
        Timber.d("$entity")
    }

    override suspend fun update(documentId: String, entity: MobileDevice) {
        collection.document(documentId).set(entity, SetOptions.merge()).await()
        Timber.d("$entity")
    }

    override suspend fun remove(documentId: String) {
        collection.document(documentId).delete().await()
        Timber.d(documentId)
    }

    override suspend fun docExists(documentId: String): Boolean {
        val entity = collection.document(documentId).get().await()
        Timber.d("${entity.exists()}")
        return entity.exists()
    }
}