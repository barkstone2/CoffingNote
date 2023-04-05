package com.note.coffee.data.dto.beans

import androidx.room.Embedded
import androidx.room.Relation
import com.note.coffee.data.entity.beans.Bean
import com.note.coffee.data.entity.beans.Origin
import com.note.coffee.data.entity.beans.Roastery

data class BeanResponse(
    @Embedded val bean: Bean,
    @Relation(
        parentColumn = "originId",
        entityColumn = "id"
    )
    var origin: Origin?,
    @Relation(
        parentColumn = "roasteryId",
        entityColumn = "id"
    )
    var roastery: Roastery?,
) {
    fun getOriginInfo(): String? {
        var result = origin?.name
        if(!bean.specificOrigin.isNullOrEmpty()) {
            result += " - " + bean.specificOrigin
        }

        return result
    }

}