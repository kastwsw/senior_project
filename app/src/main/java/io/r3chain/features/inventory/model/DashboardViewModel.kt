package io.r3chain.features.inventory.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.repositories.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class DashboardViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    /**
     * Данные авторизованного пользователя.
     */
    val currentUser = userRepository.getUserFlow()

    /**
     * Данные картинки аватара авторизованного пользователя.
     */
    val currentUserImage = userRepository.getPictureFlow()

    /**
     * Current screen state.
     */
    var currentState by mutableStateOf(ScreenState.INVENTORY)
        private set


    init {
        viewModelScope.launch {
            userRepository.refresh()
        }
    }

    enum class ScreenState {
        INVENTORY, DISPATCHED
    }

}