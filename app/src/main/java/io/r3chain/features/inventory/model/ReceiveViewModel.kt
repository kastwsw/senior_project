package io.r3chain.features.inventory.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.repositories.WasteRepository
import io.r3chain.data.vo.WasteVO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class ReceiveViewModel @Inject constructor(
    private val wasteRepository: WasteRepository
) : ViewModel() {

    /**
     * Индикатор загрузки.
     */
    var isLoading by mutableStateOf(false)
        private set


    /**
     * Данные для формы.
     */
    var data: WasteVO by mutableStateOf(WasteVO())
        private set


    fun doneForm() {
        viewModelScope.launch {
            isLoading = true
//            wasteRepository.addCollect()
            isLoading = false
        }
    }

}