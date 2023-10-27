package com.note.coffee.ui.water

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.coffee.data.entity.water.Water
import com.note.coffee.data.repository.water.WaterRepository
import com.note.coffee.ui.SharedData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WaterViewModel @Inject constructor(
    private val waterRepository: WaterRepository,
    private val sharedData: SharedData,
): ViewModel() {

    private val _uiState = MutableStateFlow(WaterUiState(sharedData = sharedData))
    val uiState: StateFlow<WaterUiState> = _uiState.asStateFlow()

    init {
        Log.d(this::class.simpleName, "init")
    }

    fun saveWater(water: Water) {
        viewModelScope.launch(Dispatchers.IO) {
            waterRepository.insert(water)
            sharedData.loadWaters()
        }
    }


    fun deleteWater(water: Water) {
        viewModelScope.launch(Dispatchers.IO) {
            waterRepository.delete(water)
            sharedData.loadWaters()
        }
    }

    fun getWater(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val water = waterRepository.get(id)
            _uiState.update {
                it.copy(water = water)
            }
        }
    }

    fun updateWater(water: Water) {
        viewModelScope.launch(Dispatchers.IO) {
            waterRepository.update(water)
            sharedData.loadWaters()
        }
    }

}