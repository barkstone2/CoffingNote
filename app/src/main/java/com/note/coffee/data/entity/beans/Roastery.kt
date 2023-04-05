package com.note.coffee.data.entity.beans

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Roastery(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String?,
    val comment: String? = null
)