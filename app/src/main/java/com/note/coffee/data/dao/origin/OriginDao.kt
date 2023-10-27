package com.note.coffee.data.dao.origin

import androidx.room.*
import com.note.coffee.data.entity.beans.Origin

@Dao
interface OriginDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(origin: Origin): Long

    @Query("SELECT * FROM origin ORDER BY orderId DESC")
    suspend fun getAll(): List<Origin>

    @Query("select * from origin where id = :id")
    suspend fun get(id: Long): Origin

    @Delete
    suspend fun delete(origin: Origin)

    @Update
    suspend fun update(origin: Origin)

    @Query("UPDATE origin SET orderId = :orderId WHERE id = :id")
    suspend fun changeOrder(id: Long, orderId: Long)

    @Query("SELECT COALESCE(MAX(orderId), 0)+1 FROM origin")
    suspend fun getNextOrderId() : Long
}