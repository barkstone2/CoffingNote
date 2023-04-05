package com.note.coffee.data.repository.beans

import com.note.coffee.data.dao.beans.BeanDao
import com.note.coffee.data.dto.beans.BeanResponse
import com.note.coffee.data.entity.beans.Bean
import javax.inject.Inject

class BeanRepository @Inject constructor(
    private val beanDao: BeanDao,
) {

    suspend fun getAll(): List<BeanResponse> {
        return beanDao.getAll()
    }

    suspend fun insert(bean: Bean) {
        beanDao.insert(bean)
    }

    suspend fun get(id: Long): BeanResponse {
        return beanDao.get(id)
    }

    suspend fun delete(bean: Bean) {
        beanDao.delete(bean)
    }

    suspend fun update(bean: Bean) {
        beanDao.update(bean)
    }

}