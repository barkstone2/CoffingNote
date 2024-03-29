package com.note.coffee.ui.recipes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.coffee.data.dto.beans.BeanResponse
import com.note.coffee.data.entity.recipes.Recipe
import com.note.coffee.data.repository.recipes.RecipeRepository
import com.note.coffee.ui.SharedData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val sharedData: SharedData,
): ViewModel() {

    private val _uiState = MutableStateFlow(RecipesUiState(sharedData = sharedData))
    val uiState: StateFlow<RecipesUiState> = _uiState.asStateFlow()

    init {
        Log.d(this::class.simpleName, "init")
    }

    fun saveRecipe(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.insert(recipe)
            sharedData.loadRecipes(recipe.beanId!!)
        }
    }

    fun deleteRecipe(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.delete(recipe)
            sharedData.loadRecipes(recipe.beanId!!)
        }
    }

    fun getRecipe(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipe = recipeRepository.get(id)
            _uiState.update {
                it.copy(recipe = recipe)
            }
        }
    }

    fun updateRecipe(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.update(recipe)
            sharedData.loadRecipes(recipe.beanId!!)
        }
    }

    fun selectRecipeBean(bean: BeanResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            sharedData.loadRecipes(bean.bean.id)
            _uiState.update {
                it.copy(bean = bean)
            }
        }
    }

    fun displayError(errorMessage: String) {
        _uiState.update {
            it.copy(errorMessage = errorMessage)
        }

        viewModelScope.launch {
            delay(2000)
            messageDismiss()
        }
    }

    private fun messageDismiss() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    fun reorderRecipe(idx: Int, otherIdx: Int) {
        if(otherIdx < 0 || otherIdx >= sharedData.recipes.value.size) return
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.reorder(sharedData.recipes.value[idx].recipe, sharedData.recipes.value[otherIdx].recipe)
            sharedData.reorderRecipe(idx, otherIdx)
            _uiState.update {
                it.copy(version = it.version+1)
            }
        }
    }
}