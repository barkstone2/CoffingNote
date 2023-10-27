package com.note.coffee.ui.drippers

import com.note.coffee.data.entity.drippers.Dripper
import com.note.coffee.ui.SharedData

data class DrippersUiState(
    val dripper: Dripper? = null,
    val sharedData: SharedData,
    val version: Int = 0
) {
    val drippers: List<Dripper>
        get() = sharedData.drippers.value

}