package io.r3chain.features.root.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
open class RootViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    /**
     * Current screen state.
     */
    var currentState by mutableStateOf(ScreenState.LOADING)
        private set


    init {
        // Начать отслеживать данные текущего пользователя.
        viewModelScope.launch(Dispatchers.IO) {
//            delay(3500)
            userRepository.getAuthTokenFlow().collectLatest {
                withContext(Dispatchers.Main) {
                    currentState = if (it.isBlank()) ScreenState.AUTH else ScreenState.INVENTORY
                }
            }
        }
    }

    enum class ScreenState {
        LOADING, AUTH, INVENTORY
    }

}