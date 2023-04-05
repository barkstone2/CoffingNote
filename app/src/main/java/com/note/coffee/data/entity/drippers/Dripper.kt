package com.note.coffee.data.entity.drippers

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Dripper(
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