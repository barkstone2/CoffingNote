package com.note.coffee.data.room.converter

import androidx.room.TypeConverter
import com.note.coffee.data.entity.beans.RoastDegree

class RoastDegreeConverter {
    @TypeConverter
    fun fromRoastDegree(roastDegree: RoastDegree?): String? {
        return roastDegree?.name
    }

    @TypeConverter
    fun toRoastDegree(name: String?): RoastDegree? {
        return if(name == null) null else RoastDegree.valueOf(name)
    }
}