package io.r3chain.features.inventory.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.core.data.vo.WasteVO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class DetailsViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel() {

    /**
     * Индикатор загрузки.
     */
    var isLoading by mutableStateOf(false)
        private set

    /**
     * Данные записи.
     */
    var data by mutableStateOf(
        WasteVO().copy(
            id = handle["waste_id"] ?: 0
        )
    )
        private set


    init {
        viewModelScope.launch {
            isLoading = true
            // TODO: обновить данные, пришедшие из списка
//            data = wasteRepository.getWaste()
            isLoading = false
        }
    }

    fun initData(value: WasteVO) {
        handle["waste_id"] = value.id
        data = value
    }

}