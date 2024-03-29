package com.note.coffee.data.dao.water

import androidx.room.*
import com.note.coffee.data.entity.water.Water

@Dao
interface WaterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(water: Water): Long

    @Query("SELECT * FROM water ORDER BY orderId DESC")
    suspend fun getAll(): List<Water>

    @Delete
    suspend fun delete(water: Water)

    @Query("select * from water where id = :id")
    suspend fun get(id: Long): Water

    @Update
    suspend fun update(water: Water)

    @Query("UPDATE water SET orderId = :orderId WHERE id = :id")
    suspend fun changeOrder(id: Long, orderId: Long)

    @Query("SELECT COALESCE(MAX(orderId), 0)+1 FROM water")
    suspend fun getNextOrderId() : Long
}