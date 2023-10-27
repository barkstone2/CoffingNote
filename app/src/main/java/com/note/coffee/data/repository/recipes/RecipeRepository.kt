package com.note.coffee.data.repository.recipes

import androidx.room.Transaction
import com.note.coffee.data.dao.recipes.RecipeDao
import com.note.coffee.data.dto.recipes.RecipeResponse
import com.note.coffee.data.entity.recipes.Recipe
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val recipeDao: RecipeDao,
) {

    suspend fun getAllOfBean(beanId: Long): List<RecipeResponse> {
        return recipeDao.getAllOfBean(beanId)
    }

    suspend fun insert(recipe: Recipe) {
        recipeDao.insert(recipe)
    }

    suspend fun get(id: Long): RecipeResponse {
        return recipeDao.get(id)
    }

    suspend fun delete(recipe: Recipe) {
        recipeDao.delete(recipe)
    }

    suspend fun update(recipe: Recipe) {
        recipeDao.update(recipe)
    }

    suspend fun deleteAllByBeanId(beanId: Long) {
        recipeDao.deleteAllByBeanId(beanId)
    }

    @Transaction
    suspend fun reorder(current: Recipe, other: Recipe) {
        val orderId = current.orderId
        val otherOrderId = other.orderId

        recipeDao.changeOrder(current.id, otherOrderId)
        recipeDao.changeOrder(other.id, orderId)
    }

}