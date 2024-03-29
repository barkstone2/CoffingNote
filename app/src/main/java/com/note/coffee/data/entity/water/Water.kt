package com.note.coffee.data.entity.water

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Water(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String?,
    val comment: String? = null,
    var orderId: Long = 0,
)