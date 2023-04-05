package com.note.coffee.data.dto.roastery

import com.note.coffee.data.entity.beans.Roastery

data class RoasteryRequest(
    val id: Long = 0,
    val name: String? = "",
    val comment: String? = ""
) {

    constructor(roastery: Roastery): this(
        id = roastery.id,
        name = roastery.name,
        comment = roastery.comment
    )

    fun mapToEntity(): Roastery {
        return Roastery(
            id = id,
            name = name,
            comment = comment
        )
    }
}