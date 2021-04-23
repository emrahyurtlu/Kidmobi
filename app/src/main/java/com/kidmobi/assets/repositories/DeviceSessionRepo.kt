package com.kidmobi.assets.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.kidmobi.assets.enums.DbCollection
import com.kidmobi.assets.utils.extensions.toDeviceSession
import com.kidmobi.assets.utils.extensions.toDeviceSessionList
import com.kidmobi.mvvm.model.DeviceSession
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DeviceSessionRepo @Inject constructor(private val db: FirebaseFirestore) : BaseRepo<DeviceSession> {

    private val collection = db.collection(DbCollection.DeviceSessions.name)

    override suspend fun getById(documentId: String): DeviceSession {
        return collection.document(documentId).get().await().toDeviceSession()
    }

    override suspend fun getList(): MutableList<DeviceSession> {
        return collection.get().await().toDeviceSessionList()
    }

    override suspend fun add(entity: DeviceSession) {
        collection.add(entity)
    }

    override suspend fun update(documentId: String, entity: DeviceSession) {
        collection.document(documentId).set(entity, SetOptions.merge()).await()
    }

    override suspend fun remove(documentId: String) {
        collection.document(documentId).delete().await()
    }

    override suspend fun docExists(documentId: String): Boolean {
        return collection.document(documentId).get().await().exists()
    }

    suspend fun sessionEnd(documentId: String) {
        val entity = getById(documentId)
        entity.done = true
        update(documentId, entity)
    }

    suspend fun getByOpenSession(sessionOwnerDeviceId: String): DeviceSession {
        return collection
            .whereEqualTo("", sessionOwnerDeviceId)
            .whereEqualTo("done", false)
            .get().await().first().toDeviceSession()
    }
}