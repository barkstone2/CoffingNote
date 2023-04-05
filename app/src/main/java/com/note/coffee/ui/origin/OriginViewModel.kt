package com.note.coffee.ui.origin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.coffee.data.entity.beans.Origin
import com.note.coffee.data.repository.beans.OriginRepository
import com.note.coffee.ui.SharedData
import dagger.hilt.android.lifecycle.HiltViewModel
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
        viewModelScope.launch {
            originRepository.insert(origin)
            sharedData.loadOrigins()
        }
    }

    fun deleteOrigin(origin: Origin) {
        viewModelScope.launch {
            originRepository.delete(origin)
            sharedData.loadOrigins()
            sharedData.loadBeans()
            sharedData.loadRecipes()
        }
    }

    fun getOrigin(id: Long) {
        viewModelScope.launch {
            val origin = originRepository.get(id)
            _uiState.update {
                it.copy(origin = origin)
            }
        }
    }

    fun updateOrigin(origin: Origin) {
        viewModelScope.launch {
            originRepository.update(origin)
            sharedData.loadOrigins()
            sharedData.loadBeans()
            sharedData.loadRecipes()
        }
    }

    fun loadOrigins() {
        viewModelScope.launch {
            sharedData.loadOrigins()
        }
    }

}