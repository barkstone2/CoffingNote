package com.note.coffee.ui.drippers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.coffee.data.entity.drippers.Dripper
import com.note.coffee.data.repository.drippers.DripperRepository
import com.note.coffee.ui.SharedData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DrippersViewModel @Inject constructor(
    private val dripperRepository: DripperRepository,
    private val sharedData: SharedData,
): ViewModel() {

    private val _uiState = MutableStateFlow(DrippersUiState(sharedData = sharedData))
    val uiState: StateFlow<DrippersUiState> = _uiState.asStateFlow()

    init {
        Log.d(this::class.simpleName, "init")

    }

    fun saveDripper(dripper: Dripper) {
        viewModelScope.launch {
            dripperRepository.insert(dripper)
            sharedData.loadDrippers()
        }
    }

    fun deleteDripper(dripper: Dripper) {
        viewModelScope.launch {
            dripperRepository.delete(dripper)
            sharedData.loadDrippers()
            sharedData.loadRecipes()
        }
    }

    fun getDripper(id: Long) {
        viewModelScope.launch {
            val dripper = dripperRepository.get(id)
            _uiState.update {
                it.copy(dripper = dripper)
            }
        }
    }

    fun updateDripper(dripper: Dripper) {
        viewModelScope.launch {
            dripperRepository.update(dripper)
            sharedData.loadDrippers()
            sharedData.loadRecipes()
        }
    }

}