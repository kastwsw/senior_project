package io.r3chain.features.inventory.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.core.data.vo.WasteEntity
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class DetailsViewModel @Inject constructor() : ViewModel() {

    /**
     * Индикатор загрузки.
     */
    var isLoading by mutableStateOf(false)
        private set

    /**
     * Данные записи.
     */
    var data by mutableStateOf(WasteEntity())
        private set


    init {
        viewModelScope.launch {
            isLoading = true
            // TODO: обновить данные, пришедшие из списка
//            data = wasteRepository.getWaste()
            isLoading = false
        }
    }

    fun initData(value: WasteEntity) {
        data = value
    }

}