package com.note.coffee.ui.water

import com.note.coffee.data.entity.water.Water
import com.note.coffee.ui.SharedData

data class WaterUiState(
    val water: Water? = null,
    val sharedData: SharedData
) {

    val waters: List<Water>
        get() = sharedData.waters.value
}