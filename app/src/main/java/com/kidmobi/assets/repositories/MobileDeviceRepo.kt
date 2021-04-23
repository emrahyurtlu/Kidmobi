package com.kidmobi.assets.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kidmobi.assets.enums.DbCollection
import com.kidmobi.assets.utils.extensions.toMobileDevice
import com.kidmobi.assets.utils.extensions.toMobileDeviceList
import com.kidmobi.assets.utils.printsln
import com.kidmobi.mvvm.model.MobileDevice
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MobileDeviceRepo @Inject constructor(
    db: FirebaseFirestore
) : BaseRepo<MobileDevice> {

    private var collection: CollectionReference = db.collection(DbCollection.MobileDevices.name)

    override suspend fun getById(documentId: String): MobileDevice {
        val result = collection.document(documentId).get().await().toMobileDevice()
        printsln(result, "MobileDeviceRepo::getById()")
        return result
    }

    override suspend fun getList(): MutableList<MobileDevice> {
        val list = collection.get().await().toMobileDeviceList()
        printsln(list, "MobileDeviceRepo::getList()")
        return list
    }

    override suspend fun add(entity: MobileDevice) {
        collection.document(entity.deviceId).set(entity, SetOptions.merge()).await()
        printsln(entity, "MobileDeviceRepo::add()")
    }

    override suspend fun update(documentId: String, entity: MobileDevice) {
        collection.document(documentId).set(entity, SetOptions.merge()).await()
        printsln(entity, "MobileDeviceRepo::update()")
    }

    override suspend fun remove(documentId: String) {
        collection.document(documentId).delete().await()
        printsln(documentId, "MobileDeviceRepo::remove()")
    }

    override suspend fun docExists(documentId: String): Boolean {
        val entity = collection.document(documentId).get().await()
        printsln(entity.exists(), "MobileDeviceRepo::docExists()")
        return entity.exists()
    }
}