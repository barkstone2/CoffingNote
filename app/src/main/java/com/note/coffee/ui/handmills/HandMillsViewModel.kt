package com.note.coffee.ui.handmills

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.coffee.data.entity.handmills.HandMill
import com.note.coffee.data.repository.handmills.HandMillRepository
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
class HandMillsViewModel @Inject constructor(
    private val handMillRepository: HandMillRepository,
    private val sharedData: SharedData,
): ViewModel() {

    private val _uiState = MutableStateFlow(HandMillsUiState(sharedData = sharedData))
    val uiState: StateFlow<HandMillsUiState> = _uiState.asStateFlow()

    init {
        Log.d(this::class.simpleName, "init")
    }

    fun saveHandMill(handMill: HandMill) {
        viewModelScope.launch(Dispatchers.IO) {
            handMillRepository.insert(handMill)
            sharedData.loadHandMills()
        }
    }


    fun deleteHandMill(handMill: HandMill) {
        viewModelScope.launch(Dispatchers.IO) {
            handMillRepository.delete(handMill)
            sharedData.loadHandMills()
            sharedData.loadRecipes()
        }
    }

    fun getHandMill(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val handMill = handMillRepository.get(id)
            _uiState.update {
                it.copy(handMill = handMill)
            }
        }
    }

    fun updateHandMill(handMill: HandMill) {
        viewModelScope.launch(Dispatchers.IO) {
            handMillRepository.update(handMill)
            sharedData.loadHandMills()
            sharedData.loadRecipes()
        }
    }

}