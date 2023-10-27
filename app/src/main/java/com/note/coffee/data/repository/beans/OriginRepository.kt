package com.note.coffee.data.repository.beans

import androidx.room.Transaction
import com.note.coffee.data.dao.origin.OriginDao
import com.note.coffee.data.entity.beans.Origin
import javax.inject.Inject

class OriginRepository @Inject constructor(
    private val originDao: OriginDao,
) {

    suspend fun getAll(): List<Origin> {
        return originDao.getAll()
    }

    @Transaction
    suspend fun insert(origin: Origin) {
        val nextOrderId = originDao.getNextOrderId()
        origin.orderId = nextOrderId
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

    @Transaction
    suspend fun reorder(current: Origin, other: Origin) {
        val orderId = current.orderId
        val otherOrderId = other.orderId

        originDao.changeOrder(current.id, otherOrderId)
        originDao.changeOrder(other.id, orderId)
    }
}