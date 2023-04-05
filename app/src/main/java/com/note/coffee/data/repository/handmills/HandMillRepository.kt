package com.note.coffee.data.repository.handmills

import com.note.coffee.data.dao.handmills.HandMillDao
import com.note.coffee.data.entity.handmills.HandMill
import javax.inject.Inject

class HandMillRepository @Inject constructor(
    private val handMillDao: HandMillDao,
) {

    suspend fun getAll(): List<HandMill> {
        return handMillDao.getAll()
    }

    suspend fun insert(handMill: HandMill) {
        handMillDao.insert(handMill)
    }

    suspend fun get(id: Long): HandMill {
        return handMillDao.get(id)
    }

    suspend fun delete(handMill: HandMill) {
        handMillDao.delete(handMill)
    }

    suspend fun update(handMill: HandMill) {
        handMillDao.update(handMill)
    }

}