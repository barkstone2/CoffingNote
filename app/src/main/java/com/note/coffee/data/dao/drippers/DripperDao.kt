package com.note.coffee.data.dao.drippers

import androidx.room.*
import com.note.coffee.data.entity.drippers.Dripper

@Dao
interface DripperDao {

    @Query("SELECT * FROM dripper ORDER BY orderId DESC")
    suspend fun getAll(): List<Dripper>

    @Query("select * from dripper where id = :id")
    suspend fun get(id: Long): Dripper

    @Insert
    suspend fun insert(dripper: Dripper): Long

    @Delete
    suspend fun delete(dripper: Dripper)

    @Update
    suspend fun update(dripper: Dripper)

}