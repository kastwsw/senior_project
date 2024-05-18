package io.r3chain.features.auth.model

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.R
import io.r3chain.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
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

    var isRemember = userRepository.getRememberMeFlow()
        private set


    fun changeLogin(value: String) {
        login = value
    }

    fun changePassword(value: String) {
        password = value
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

    fun signUp(context: Context) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(context.getString(R.string.sign_up_link))
            )
        )
    }

}