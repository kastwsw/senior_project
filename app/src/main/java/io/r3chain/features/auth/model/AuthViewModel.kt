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
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

//    var login by mutableStateOf("")
    var login by mutableStateOf("test3@example.com")
        private set

//    var password by mutableStateOf("")
    var password by mutableStateOf("test_pass")
        private set

    var isRemember by mutableStateOf(true)
        private set


    fun changeLogin(value: String) {
        login = value
    }

    fun changePassword(value: String) {
        password = value
    }

    fun changeIsRemember(value: Boolean) {
        isRemember = value
    }

    fun signIn() {
        viewModelScope.launch {
            isLoading = true
            userRepository.authUser(email = login, password = password)
            isLoading = false
        }
    }

}