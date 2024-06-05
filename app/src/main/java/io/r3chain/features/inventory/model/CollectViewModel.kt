package io.r3chain.features.inventory.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.repositories.WasteRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class CollectViewModel @Inject constructor(
    private val wasteRepository: WasteRepository
) : ViewModel() {

    /**
     * Индикатор загрузки.
     */
    var isLoading by mutableStateOf(false)
        private set


    fun doneForm() {
        viewModelScope.launch {
            isLoading = true
            wasteRepository.addCollect()
            isLoading = false
        }
    }

}