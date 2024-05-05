package io.r3chain.features.auth.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.r3chain.data.repositories.UserRepository
import io.r3chain.navigation.SharedModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthModel : ViewModel() {

    private val userRepository by lazy {
        UserRepository()
    }

    fun signIn(email: String, password: String, model: SharedModel) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUser(email = email, password = password)?.also {
                model.updateUser(it)
            }
        }
    }
}