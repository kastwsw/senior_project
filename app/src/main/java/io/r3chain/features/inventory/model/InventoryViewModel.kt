package io.r3chain.features.inventory.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.repositories.UserRepository
import javax.inject.Inject

@HiltViewModel
open class InventoryViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    /**
     * Current screen state.
     */
    var currentState by mutableStateOf(ScreenState.HOME)
        private set


    fun navigateToHome() {
        currentState = ScreenState.HOME
    }

    fun navigateToProfile() {
        currentState = ScreenState.PROFILE
    }

    enum class ScreenState {
        HOME, PROFILE
    }

}