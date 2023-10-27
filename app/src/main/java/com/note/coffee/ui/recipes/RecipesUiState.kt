package com.note.coffee.ui.recipes

import com.note.coffee.data.dto.beans.BeanResponse
import com.note.coffee.data.dto.recipes.RecipeRequest
import com.note.coffee.data.dto.recipes.RecipeResponse
import com.note.coffee.data.entity.drippers.Dripper
import com.note.coffee.data.entity.handmills.HandMill
import com.note.coffee.data.entity.water.Water
import com.note.coffee.ui.SharedData

data class RecipesUiState(
    val newRecipe: RecipeRequest = RecipeRequest(),
    val recipe: RecipeResponse? = null,
    val bean: BeanResponse? = null,
    val errorMessage: String? = null,
    val sharedData: SharedData,
    val version: Int = 0
) {
    val recipes: List<RecipeResponse>
        get() = sharedData.recipes.value
    val beans: List<BeanResponse>
        get() = sharedData.beans.value
    val drippers: List<Dripper>
        get() = sharedData.drippers.value
    val handMills: List<HandMill>
        get() = sharedData.handMills.value
    val waters: List<Water>
        get() = sharedData.waters.value
}