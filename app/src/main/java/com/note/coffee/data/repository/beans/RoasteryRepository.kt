package com.note.coffee.data.repository.beans

import androidx.room.Transaction
import com.note.coffee.data.dao.roastery.RoasteryDao
import com.note.coffee.data.entity.beans.Roastery
import javax.inject.Inject

class RoasteryRepository @Inject constructor(
    private val roasteryDao: RoasteryDao,
) {

    suspend fun getAll(): List<Roastery> {
        return roasteryDao.getAll()
    }

    suspend fun insert(roastery: Roastery) {
        roasteryDao.insert(roastery)
    }

    suspend fun get(id: Long): Roastery {
        return roasteryDao.get(id)
    }

    suspend fun delete(roastery: Roastery) {
        roasteryDao.delete(roastery)
    }

    suspend fun update(roastery: Roastery) {
        roasteryDao.update(roastery)
    }

    @Transaction
    suspend fun reorder(current: Roastery, other: Roastery) {
        val orderId = current.orderId
        val otherOrderId = other.orderId

        roasteryDao.changeOrder(current.id, otherOrderId)
        roasteryDao.changeOrder(other.id, orderId)
    }

}