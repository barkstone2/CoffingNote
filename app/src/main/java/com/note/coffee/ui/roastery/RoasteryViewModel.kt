package com.note.coffee.ui.roastery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.coffee.data.entity.beans.Roastery
import com.note.coffee.data.repository.beans.RoasteryRepository
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
class RoasteryViewModel @Inject constructor(
    private val roasteryRepository: RoasteryRepository,
    private val sharedData: SharedData,
): ViewModel() {

    private val _uiState = MutableStateFlow(RoasteryUiState(sharedData = sharedData))
    val uiState: StateFlow<RoasteryUiState> = _uiState.asStateFlow()

    init {
        Log.d(this::class.simpleName, "init")
    }

    fun saveRoastery(roastery: Roastery) {
        viewModelScope.launch(Dispatchers.IO) {
            roasteryRepository.insert(roastery)
            sharedData.loadRoasteries()
        }
    }


    fun deleteRoastery(roastery: Roastery) {
        viewModelScope.launch(Dispatchers.IO) {
            roasteryRepository.delete(roastery)
            sharedData.loadRoasteries()
            sharedData.loadBeans()
        }
    }

    fun getRoastery(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val roastery = roasteryRepository.get(id)
            _uiState.update {
                it.copy(roastery = roastery)
            }
        }
    }

    fun updateRoastery(roastery: Roastery) {
        viewModelScope.launch(Dispatchers.IO) {
            roasteryRepository.update(roastery)
            sharedData.loadRoasteries()
            sharedData.loadBeans()
        }
    }

}