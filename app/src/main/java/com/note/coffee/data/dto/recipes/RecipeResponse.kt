package com.note.coffee.data.dto.recipes

import androidx.room.Embedded
import androidx.room.Relation
import com.note.coffee.data.dto.beans.BeanResponse
import com.note.coffee.data.entity.beans.Bean
import com.note.coffee.data.entity.drippers.Dripper
import com.note.coffee.data.entity.handmills.HandMill
import com.note.coffee.data.entity.recipes.Recipe

data class RecipeResponse(
    @Embedded val recipe: Recipe,

    @Relation(
        entity = Bean::class,
        parentColumn = "beanId",
        entityColumn = "id"
    )
    var bean: BeanResponse?,

    @Relation(
        parentColumn = "dripperId",
        entityColumn = "id"
    )
    var dripper: Dripper?,

    @Relation(
        parentColumn = "handMillId",
        entityColumn = "id"
    )
    var handMill: HandMill?,
)