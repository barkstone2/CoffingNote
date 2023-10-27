package com.note.coffee.data.repository.water

import androidx.room.Transaction
import com.note.coffee.data.dao.water.WaterDao
import com.note.coffee.data.entity.water.Water
import javax.inject.Inject

class WaterRepository @Inject constructor(
    private val waterDao: WaterDao,
) {

    suspend fun getAll(): List<Water> {
        return waterDao.getAll()
    }

    @Transaction
    suspend fun insert(water: Water) {
        val nextOrderId = waterDao.getNextOrderId()
        water.orderId = nextOrderId
        waterDao.insert(water)
    }

    suspend fun get(id: Long): Water {
        return waterDao.get(id)
    }

    suspend fun delete(water: Water) {
        waterDao.delete(water)
    }

    suspend fun update(water: Water) {
        waterDao.update(water)
    }

    @Transaction
    suspend fun reorder(current: Water, other: Water) {
        val orderId = current.orderId
        val otherOrderId = other.orderId

        waterDao.changeOrder(current.id, otherOrderId)
        waterDao.changeOrder(other.id, orderId)
    }
}