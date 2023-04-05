package com.note.coffee.data.entity.handmills

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hand_mill")
data class HandMill(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String?,
    val createdBy: String?,
    val comment: String? = null
) {
    fun getFullName(): String? {
        var result = name
        if(createdBy != null) {
            result += " - $createdBy"
        }
        return result
    }
}