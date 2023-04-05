package com.note.coffee.ui.origin

import com.note.coffee.data.entity.beans.Origin
import com.note.coffee.ui.SharedData

data class OriginUiState(
    val origin: Origin? = null,
    val sharedData: SharedData
) {

    val origins: List<Origin>
        get() = sharedData.origins.value
}