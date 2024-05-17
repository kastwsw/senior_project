package io.r3chain.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.services.ApiService
import io.r3chain.data.services.NetworkService
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(
    private val networkService: NetworkService,
    apiService: ApiService
) : ViewModel() {

    /**
     * Есть доступ в интерент или нет.
     */
    var hasConnection by mutableStateOf(true)
        private set

    /**
     * Flow с ошибками обращения к API.
     */
    val apiErrors = apiService.exceptionsFlow

    init {
        // Доступ в Интернет
        viewModelScope.launch {
            // не реагировать не мелкие изменения состояния
            @OptIn(FlowPreview::class)
            networkService.internetAvailableFlow
                .debounce(800)
                .distinctUntilChanged()
                .collect {
                    hasConnection = it
                }
        }
    }

}