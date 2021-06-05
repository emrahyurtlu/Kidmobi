package com.kidmobi.data.repositories

import com.kidmobi.data.model.BaseModel

interface BaseRepo<T : BaseModel> {
    suspend fun getById(documentId: String): T

    suspend fun getList(): MutableList<T>

    suspend fun add(entity: T)

    suspend fun update(documentId: String, entity: T)

    suspend fun remove(documentId: String)

    suspend fun docExists(documentId: String): Boolean
}