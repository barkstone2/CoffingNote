package com.note.coffee.ui.roastery

import com.note.coffee.data.entity.beans.Roastery
import com.note.coffee.ui.SharedData

data class RoasteryUiState(
    val roastery: Roastery? = null,
    val sharedData: SharedData,
    val version: Int = 0
) {

    val roasteries: List<Roastery>
        get() = sharedData.roasteries.value
}