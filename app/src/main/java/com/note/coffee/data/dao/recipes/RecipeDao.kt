package com.note.coffee.data.dao.recipes

import androidx.room.*
import com.note.coffee.data.dto.recipes.RecipeResponse
import com.note.coffee.data.entity.recipes.Recipe

@Dao
interface RecipeDao {

    @Transaction
    @Query("SELECT * FROM recipe ORDER BY id DESC")
    suspend fun getAll(): List<RecipeResponse>

    @Transaction
    @Query("select * from recipe where id = :id")
    suspend fun get(id: Long): RecipeResponse

    @Insert
    suspend fun insert(recipe: Recipe)

    @Delete
    suspend fun delete(recipe: Recipe)

    @Update
    suspend fun update(recipe: Recipe)

    @Query("delete from recipe where beanId = :beanId")
    suspend fun deleteAllByBeanId(beanId: Long)

}