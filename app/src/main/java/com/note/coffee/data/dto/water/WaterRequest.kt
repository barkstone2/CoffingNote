package com.note.coffee.data.dto.water

import com.note.coffee.data.entity.water.Water

data class WaterRequest(
    val id: Long = 0,
    val name: String? = "",
    val comment: String? = ""
) {

    constructor(water: Water): this(
        id = water.id,
        name = water.name,
        comment = water.comment
    )

    fun mapToEntity(): Water {
        return Water(
            id = id,
            name = name,
            comment = comment
        )
    }
}