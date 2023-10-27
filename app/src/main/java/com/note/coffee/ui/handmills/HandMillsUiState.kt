package com.note.coffee.ui.handmills

import com.note.coffee.data.entity.handmills.HandMill
import com.note.coffee.ui.SharedData

data class HandMillsUiState(
    val handMill: HandMill? = null,
    val sharedData: SharedData,
    val version: Int = 0
) {
    val handMills: List<HandMill>
        get() = sharedData.handMills.value
}