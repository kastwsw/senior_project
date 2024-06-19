package io.r3chain.features.auth.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.core.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    /**
     * Индикация процесса загрузки.
     */
    var isLoading by mutableStateOf(false)
        private set

    /**
     * Вкл/выкл кнопки входа.
     */
    var isFormValid by mutableStateOf(false)
        private set

    /**
     * Значения поля логина.
     */
    var login by mutableStateOf("test3@example.com")
        private set

    /**
     * Значения поля пароля.
     */
    var password by mutableStateOf("test_pass")
        private set

    /**
     * Вкл/выкл запоминать меня.
     */
    var isRemember = userRepository.getRememberMeFlow()
        private set


    init {
        formValidation()
    }

    fun changeLogin(value: String) {
        login = value
        formValidation()
    }

    fun changePassword(value: String) {
        password = value
        formValidation()
    }

    private fun formValidation() {
        isFormValid = login.isNotBlank() && password.isNotBlank()
    }

    fun changeIsRemember(value: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.saveRememberMe(value)
        }
    }

    fun signIn() {
        viewModelScope.launch {
            isLoading = true
            userRepository.authUser(email = login, password = password)
            isLoading = false
        }
    }

}