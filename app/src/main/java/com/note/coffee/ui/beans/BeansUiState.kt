package com.note.coffee.ui.beans

import com.note.coffee.data.dto.beans.BeanRequest
import com.note.coffee.data.dto.beans.BeanResponse
import com.note.coffee.data.entity.beans.Origin
import com.note.coffee.data.entity.beans.Roastery
import com.note.coffee.ui.SharedData

data class BeansUiState(
    val newBean: BeanRequest = BeanRequest(),
    val bean: BeanResponse? = null,
    val sharedData: SharedData,
    val version: Int = 0
) {

    val beans: List<BeanResponse>
        get() = sharedData.beans.value
    val origins: List<Origin>
        get() = sharedData.origins.value
    val roasteries: List<Roastery>
        get() = sharedData.roasteries.value

}