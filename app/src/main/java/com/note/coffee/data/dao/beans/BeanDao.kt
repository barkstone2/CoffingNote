package com.note.coffee.data.dao.beans

import androidx.room.*
import com.note.coffee.data.dto.beans.BeanResponse
import com.note.coffee.data.entity.beans.Bean

@Dao
interface BeanDao {

    @Transaction
    @Query("SELECT * FROM bean ORDER BY orderId DESC")
    suspend fun getAll(): List<BeanResponse>

    @Transaction
    @Query("select * from bean where id = :id")
    suspend fun get(id: Long): BeanResponse

    @Insert
    suspend fun insert(bean: Bean)

    @Delete
    suspend fun delete(bean: Bean)

    @Update
    suspend fun update(bean: Bean)

    @Query("UPDATE bean SET orderId = :orderId WHERE id = :id")
    suspend fun changeOrder(id: Long, orderId: Long)

    @Query("SELECT COALESCE(MAX(orderId), 0)+1 FROM bean")
    suspend fun getNextOrderId() : Long
}