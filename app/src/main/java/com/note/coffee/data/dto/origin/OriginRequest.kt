package com.note.coffee.data.dto.origin

import com.note.coffee.data.entity.beans.Origin

data class OriginRequest(
    val id: Long = 0,
    val name: String? = "",
) {

    constructor(origin: Origin): this(
        id = origin.id,
        name = origin.name,
    )

    fun mapToEntity(): Origin {
        return Origin(
            id = id,
            name = name,
        )
    }
}