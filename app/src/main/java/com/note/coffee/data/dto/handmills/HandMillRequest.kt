package com.note.coffee.data.dto.handmills

import com.note.coffee.data.entity.handmills.HandMill

data class HandMillRequest(
    val id: Long = 0,
    val name: String? = "",
    val createdBy: String? = "",
    val comment: String? = ""
) {

    constructor(handMill: HandMill): this(
        id = handMill.id,
        name = handMill.name,
        createdBy = handMill.createdBy,
        comment = handMill.comment ?: ""
    )

    fun mapToEntity(): HandMill {
        return HandMill(
            id = id,
            name = name,
            createdBy = createdBy,
            comment = comment
        )
    }
}