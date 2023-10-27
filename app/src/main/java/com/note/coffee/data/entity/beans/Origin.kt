package com.note.coffee.data.entity.beans

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Origin(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String?,
    var orderId: Long = 0,
) {
}