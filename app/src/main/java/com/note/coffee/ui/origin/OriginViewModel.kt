package com.note.coffee.ui.origin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.coffee.data.entity.beans.Origin
import com.note.coffee.data.repository.beans.OriginRepository
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
class OriginViewModel @Inject constructor(
    private val originRepository: OriginRepository,
    private val sharedData: SharedData,
): ViewModel() {

    private val _uiState = MutableStateFlow(OriginUiState(sharedData = sharedData))
    val uiState: StateFlow<OriginUiState> = _uiState.asStateFlow()

    init {
        Log.d(this::class.simpleName, "init")
    }

    fun saveOrigin(origin: Origin) {
        viewModelScope.launch(Dispatchers.IO) {
            originRepository.insert(origin)
            sharedData.loadOrigins()
        }
    }

    fun deleteOrigin(origin: Origin) {
        viewModelScope.launch(Dispatchers.IO) {
            originRepository.delete(origin)
            sharedData.loadOrigins()
            sharedData.loadBeans()
        }
    }

    fun getOrigin(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val origin = originRepository.get(id)
            _uiState.update {
                it.copy(origin = origin)
            }
        }
    }

    fun updateOrigin(origin: Origin) {
        viewModelScope.launch(Dispatchers.IO) {
            originRepository.update(origin)
            sharedData.loadOrigins()
            sharedData.loadBeans()
        }
    }

    fun loadOrigins() {
        viewModelScope.launch(Dispatchers.IO) {
            sharedData.loadOrigins()
        }
    }

    fun reorderOrigin(idx: Int, otherIdx: Int) {
        if(otherIdx < 0 || otherIdx >= sharedData.origins.value.size) return
        viewModelScope.launch(Dispatchers.IO) {
            originRepository.reorder(sharedData.origins.value[idx], sharedData.origins.value[otherIdx])
            sharedData.reorderOrigin(idx, otherIdx)
            _uiState.update {
                it.copy(version = it.version+1)
            }
        }
    }
}