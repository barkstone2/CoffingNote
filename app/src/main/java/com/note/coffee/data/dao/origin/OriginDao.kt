package com.note.coffee.data.dao.origin

import androidx.room.*
import com.note.coffee.data.entity.beans.Origin

@Dao
interface OriginDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(origin: Origin): Long

    @Query("SELECT * FROM origin")
    suspend fun getAll(): List<Origin>

    @Query("select * from origin where id = :id")
    suspend fun get(id: Long): Origin

    @Delete
    suspend fun delete(origin: Origin)

    @Update
    suspend fun update(origin: Origin)
}