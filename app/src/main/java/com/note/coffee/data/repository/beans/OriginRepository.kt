package com.note.coffee.data.repository.beans

import com.note.coffee.data.dao.origin.OriginDao
import com.note.coffee.data.entity.beans.Origin
import javax.inject.Inject

class OriginRepository @Inject constructor(
    private val originDao: OriginDao,
) {

    suspend fun getAll(): List<Origin> {
        return originDao.getAll()
    }

    suspend fun insert(origin: Origin) {
        originDao.insert(origin)
    }

    suspend fun get(id: Long): Origin {
        return originDao.get(id)
    }

    suspend fun delete(origin: Origin) {
        originDao.delete(origin)
    }

    suspend fun update(origin: Origin) {
        originDao.update(origin)
    }

}