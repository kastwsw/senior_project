package io.r3chain.features.root.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.repositories.UserRepository
import io.r3chain.data.services.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
open class RootViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val networkService: NetworkService,
    private val userRepository: UserRepository
) : ViewModel() {

    /**
     * Current screen state.
     */
    var currentState by mutableStateOf(ScreenState.LOADING)
        private set

    /**
     * Есть доступ в интерент или нет.
     */
    var hasConnection by mutableStateOf(true)
        private set

    /**
     * Flow с ошибками обращения к API.
     */
    val apiErrors = networkService.exceptionsFlow


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

        // Начать отслеживать данные текущего пользователя.
        viewModelScope.launch(Dispatchers.IO) {
            delay(3500)
            userRepository.getAuthTokenFlow().collectLatest {
                withContext(Dispatchers.Main) {
                    currentState = if (it.isBlank()) ScreenState.AUTH else ScreenState.INSIDE
                }
            }
        }
    }

    enum class ScreenState {
        LOADING, AUTH, INSIDE
    }

}