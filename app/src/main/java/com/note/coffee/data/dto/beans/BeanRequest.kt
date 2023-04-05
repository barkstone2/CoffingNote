package com.note.coffee.data.dto.beans

import com.note.coffee.data.entity.beans.Bean
import com.note.coffee.data.entity.beans.Origin
import com.note.coffee.data.entity.beans.RoastDegree
import com.note.coffee.data.entity.beans.Roastery


data class BeanRequest(
    val id: Long = 0,
    val name: String? = "",
    val origin: Origin? = null,
    val specificOrigin: String? = "",
    val roastery: Roastery? = null,
    val roastDegree: RoastDegree? = null,
    val cuppingNotes: MutableList<String> = mutableListOf(),
    val comment: String = "",
) {

    constructor(bean: BeanResponse): this(
        id = bean.bean.id,
        name = bean.bean.name,
        origin = bean.origin,
        specificOrigin = bean.bean.specificOrigin,
        roastery = bean.roastery,
        roastDegree = bean.bean.roastDegree,
        cuppingNotes = bean.bean.cuppingNotes as MutableList<String>,
        comment = bean.bean.comment ?: ""
    )


    fun mapToEntity(): Bean {
        return Bean(
            id = id,
            name = name,
            originId = origin?.id,
            specificOrigin = specificOrigin,
            roasteryId = roastery?.id,
            roastDegree = roastDegree,
            cuppingNotes = cuppingNotes,
            comment = comment
        )
    }
}