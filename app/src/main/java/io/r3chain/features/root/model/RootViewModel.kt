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
import io.r3chain.data.vo.UserVO
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
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
    var currentScreen by mutableStateOf(ScreenState.LOADING)
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

    /**
     * Данные авторизованного пользователя.
     */
    var currentUser: UserVO? by mutableStateOf(null)
        private set

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
        viewModelScope.launch {
//            delay(3500)
            userRepository.getUserFlow().collectLatest {
                currentScreen = if (it != null) ScreenState.INSIDE else ScreenState.AUTH
                currentUser = it
            }
        }
    }

    enum class ScreenState {
        LOADING, AUTH, INSIDE
    }

}