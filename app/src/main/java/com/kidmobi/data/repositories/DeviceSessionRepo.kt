package com.kidmobi.data.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.kidmobi.business.utils.enums.DbCollection
import com.kidmobi.business.utils.extensions.toDeviceSession
import com.kidmobi.business.utils.extensions.toDeviceSessionList
import com.kidmobi.data.model.DeviceSession
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class DeviceSessionRepo @Inject constructor(db: FirebaseFirestore) : BaseRepo<DeviceSession> {

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


    suspend fun getByOpenSession(sessionOwnerDeviceId: String): DeviceSession {
        var temp = DeviceSession()
        val calendar = Calendar.getInstance()
        val result = collection
            .whereEqualTo("sessionOwnerDeviceId", sessionOwnerDeviceId)
            .whereGreaterThan("sessionEnd", calendar.time)
            .orderBy("sessionEnd", Query.Direction.DESCENDING)
            .get().await().toDeviceSessionList()

        Timber.d("SessionList : $result")

        if (result.size > 0)
            temp = result.first()

        return temp
    }
}