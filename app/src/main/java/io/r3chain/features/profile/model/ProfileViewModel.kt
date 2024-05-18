package io.r3chain.features.profile.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.repositories.UserRepository
import io.r3chain.data.vo.UserVO
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    /**
     * Данные авторизованного пользователя.
     */
    var currentUser: UserVO? by mutableStateOf(null)
        private set

    var isLoading by mutableStateOf(false)
        private set


    init {
        // Начать отслеживать данные текущего пользователя.
        viewModelScope.launch {
            userRepository.getUserFlow().collectLatest {
                currentUser = it
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            isLoading = true
            userRepository.exit()
            isLoading = false
        }
    }

    fun refreshUserData() {
        viewModelScope.launch {
            isLoading = true
            userRepository.refresh()
            isLoading = false
        }
    }

}