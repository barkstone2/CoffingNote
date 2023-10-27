package com.note.coffee.data.dao.roastery

import androidx.room.*
import com.note.coffee.data.entity.beans.Roastery

@Dao
interface RoasteryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(roastery: Roastery): Long

    @Query("SELECT * FROM roastery ORDER BY orderId DESC")
    suspend fun getAll(): List<Roastery>

    @Delete
    suspend fun delete(roastery: Roastery)

    @Query("select * from roastery where id = :id")
    suspend fun get(id: Long): Roastery

    @Update
    suspend fun update(roastery: Roastery)

}