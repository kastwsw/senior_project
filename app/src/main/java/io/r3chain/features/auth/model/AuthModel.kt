package io.r3chain.features.auth.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val userRepository: UserRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            userRepository.fetchUser(email = email, password = password)
            isLoading = false
        }
    }
}