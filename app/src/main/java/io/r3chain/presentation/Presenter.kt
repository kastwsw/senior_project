package io.r3chain.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.r3chain.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Presenter : ViewModel() {

    private val userRepository by lazy {
        UserRepository()
    }

    var currentUser by mutableStateOf<io.r3chain.data.vo.UserVO?>(null)

    fun signIn(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser = userRepository.getUser(email = email, password = password)
        }
    }
}