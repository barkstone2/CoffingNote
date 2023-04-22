package com.note.coffee.data.entity.recipes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val beanId: Long?,
    val dripperId: Long?,
    val handMillId: Long?,
    val waterId: Long?,
    val temperature: Int?,
    val grindingDegree: Int?,
    val waterRatio: Float?,
    val beenRatio: Float?,
    val comment: String? = null
)