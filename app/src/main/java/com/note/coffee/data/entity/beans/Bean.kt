package com.note.coffee.data.entity.beans

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bean(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String?,
    val originId: Long?,
    val specificOrigin: String?,
    val roasteryId: Long?,
    val roastDegree: RoastDegree?,
    val cuppingNotes: List<String>,
    val comment: String? = null,
    var orderId: Long = 0,
) {

}