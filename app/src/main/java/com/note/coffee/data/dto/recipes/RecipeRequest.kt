package com.note.coffee.data.dto.recipes

import com.note.coffee.data.entity.beans.Bean
import com.note.coffee.data.entity.drippers.Dripper
import com.note.coffee.data.entity.handmills.HandMill
import com.note.coffee.data.entity.recipes.Recipe

data class RecipeRequest(
    val id: Long = 0,
    val bean: Bean? = null,
    val dripper: Dripper? = null,
    val handMill: HandMill? = null,
    val temperature: Int? = null,
    val grindingDegree: Int? = null,
    val comment: String = "",
) {
    constructor(recipe: RecipeResponse): this(
        id = recipe.recipe.id,
        bean = recipe.bean?.bean,
        dripper = recipe.dripper,
        handMill = recipe.handMill,
        temperature = recipe.recipe.temperature,
        grindingDegree = recipe.recipe.grindingDegree,
        comment = recipe.recipe.comment ?: ""
    )

    fun mapToEntity(): Recipe {
        return Recipe(
            id = id,
            beanId = bean?.id,
            dripperId = dripper?.id,
            handMillId = handMill?.id,
            temperature = temperature,
            grindingDegree = grindingDegree,
            comment = comment
        )
    }
}