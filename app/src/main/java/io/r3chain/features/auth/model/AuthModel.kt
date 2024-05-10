package io.r3chain.features.auth.model

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
class AuthModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            userRepository.authUser(email = email, password = password)
            isLoading = false
        }
    }

}