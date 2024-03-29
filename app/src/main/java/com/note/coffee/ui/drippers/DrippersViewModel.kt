package com.note.coffee.ui.drippers

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.coffee.data.entity.drippers.Dripper
import com.note.coffee.data.repository.drippers.DripperRepository
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
        viewModelScope.launch(Dispatchers.IO) {
            dripperRepository.insert(dripper)
            sharedData.loadDrippers()
        }
    }

    fun deleteDripper(dripper: Dripper) {
        viewModelScope.launch(Dispatchers.IO) {
            dripperRepository.delete(dripper)
            sharedData.loadDrippers()
        }
    }

    fun getDripper(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val dripper = dripperRepository.get(id)
            _uiState.update {
                it.copy(dripper = dripper)
            }
        }
    }

    fun updateDripper(dripper: Dripper) {
        viewModelScope.launch(Dispatchers.IO) {
            dripperRepository.update(dripper)
            sharedData.loadDrippers()
        }
    }

    fun reorderDripper(idx: Int, otherIdx: Int) {
        if(otherIdx < 0 || otherIdx >= sharedData.drippers.value.size) return
        viewModelScope.launch(Dispatchers.IO) {
            dripperRepository.reorder(sharedData.drippers.value[idx], sharedData.drippers.value[otherIdx])
            sharedData.reorderDripper(idx, otherIdx)
            _uiState.update {
                it.copy(version = it.version+1)
            }
        }
    }
}