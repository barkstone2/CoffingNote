package com.note.coffee.data.dto.drippers

import com.note.coffee.data.entity.drippers.Dripper

data class DripperRequest(
    val id: Long = 0,
    val name: String? = "",
    val createdBy: String? = "",
    val comment: String? = ""
) {

    constructor(dripper: Dripper): this(
        id = dripper.id,
        name = dripper.name,
        createdBy = dripper.createdBy,
        comment = dripper.comment ?: ""
    )

    fun mapToEntity(): Dripper {
        return Dripper(
            id = id,
            name = name,
            createdBy = createdBy,
            comment = comment
        )
    }
}