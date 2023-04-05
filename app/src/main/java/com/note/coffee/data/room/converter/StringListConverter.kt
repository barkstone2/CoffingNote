package com.note.coffee.data.room.converter

import androidx.room.TypeConverter

class StringListConverter {
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return list.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(joinedString: String): List<String> {
        return joinedString.split(",").filter { it.isNotBlank() }
    }
}
