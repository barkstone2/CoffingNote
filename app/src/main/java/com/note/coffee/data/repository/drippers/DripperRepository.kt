package com.note.coffee.data.repository.drippers

import com.note.coffee.data.dao.drippers.DripperDao
import com.note.coffee.data.entity.drippers.Dripper
import javax.inject.Inject

class DripperRepository @Inject constructor(
    private val dripperDao: DripperDao,
) {

    suspend fun getAll(): List<Dripper> {
        return dripperDao.getAll()
    }

    suspend fun insert(dripper: Dripper) {
        dripperDao.insert(dripper)
    }

    suspend fun get(id: Long): Dripper {
        return dripperDao.get(id)
    }

    suspend fun delete(dripper: Dripper) {
        dripperDao.delete(dripper)
    }

    suspend fun update(dripper: Dripper) {
        dripperDao.update(dripper)
    }

}