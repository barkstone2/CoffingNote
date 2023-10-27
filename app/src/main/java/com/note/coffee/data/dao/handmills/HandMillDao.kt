package com.note.coffee.data.dao.handmills

import androidx.room.*
import com.note.coffee.data.entity.handmills.HandMill

@Dao
interface HandMillDao {

    @Query("SELECT * FROM hand_mill ORDER BY orderId DESC")
    suspend fun getAll(): List<HandMill>

    @Query("select * from hand_mill where id = :id")
    suspend fun get(id: Long): HandMill

    @Insert
    suspend fun insert(handMill: HandMill): Long

    @Delete
    suspend fun delete(handMill: HandMill)

    @Update
    suspend fun update(handMill: HandMill)

}