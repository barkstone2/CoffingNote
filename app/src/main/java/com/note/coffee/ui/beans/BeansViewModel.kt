package com.note.coffee.ui.beans

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.note.coffee.data.dto.beans.BeanRequest
import com.note.coffee.data.entity.beans.Bean
import com.note.coffee.data.repository.beans.BeanRepository
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
class BeansViewModel @Inject constructor(
    private val beanRepository: BeanRepository,
    private val sharedData: SharedData,
): ViewModel() {

    private val _uiState = MutableStateFlow(BeansUiState(sharedData = sharedData))
    val uiState: StateFlow<BeansUiState> = _uiState.asStateFlow()

    init {
        Log.d(this::class.simpleName, "init")
    }

    fun saveBean(bean: Bean) {
        viewModelScope.launch(Dispatchers.IO) {
            beanRepository.insert(bean)
            sharedData.loadBeans()
        }
    }

    fun deleteBean(bean: Bean) {
        viewModelScope.launch(Dispatchers.IO) {
            beanRepository.delete(bean)
            sharedData.loadRecipes()
            sharedData.loadBeans()
        }
    }

    fun getBean(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val bean = beanRepository.get(id)
            _uiState.update {
                it.copy(bean = bean)
            }
        }
    }

    fun updateBean(bean: Bean) {
        viewModelScope.launch(Dispatchers.IO) {
            beanRepository.update(bean)
            sharedData.loadBeans()
            sharedData.loadRecipes()
        }
    }

    fun valueChanged(newBean: BeanRequest) {
        _uiState.update {
            it.copy(newBean = newBean)
        }
    }


}