package io.r3chain.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.repositories.UserRepository
import io.r3chain.data.vo.UserVO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class SharedModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val userRepository: UserRepository
) : ViewModel() {

    // TODO: запрашивать где-нить позже инициализации
    fun initUser() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUserFlow().collect {
                currentUser = it
            }
        }
    }

    var currentUser by mutableStateOf<UserVO?>(null)
        private set

    // TODO: делать это через апдейт БД (хз что с "не запоминать меня")
    fun updateUser(value: UserVO) {
        currentUser = value
    }

}