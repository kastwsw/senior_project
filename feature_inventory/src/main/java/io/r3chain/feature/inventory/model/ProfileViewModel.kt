package io.r3chain.feature.inventory.model

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.core.data.repositories.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    /**
     * Данные авторизованного пользователя.
     */
    val currentUser = userRepository.getUserFlow()

    /**
     * Данные картинки аватара авторизованного пользователя.
     */
    val currentUserExt = userRepository.getUserExtFlow()

    /**
     * Индикатор загрузки данных.
     */
    var isLoading by mutableStateOf(false)
        private set

    /**
     * Индикатор загрузки картинки аватара на сервер.
     */
    var isImageUploading by mutableStateOf(false)
        private set


    init {
        viewModelScope.launch {
            isLoading = true
            userRepository.refresh()
            isLoading = false
        }
    }

    fun signOut() {
        viewModelScope.launch {
            isLoading = true
            userRepository.exit()
            // NOTE: экран будет закрыт при любом результате обращения к серверу
            // NOTE: кнопки и прочее не энейблить от дурака
//            isLoading = false
        }
    }

    fun setEmailNotification(enabled: Boolean) {
        viewModelScope.launch {
            isLoading = true
            userRepository.updateUserNotification(enabledEmail = enabled)
            isLoading = false
        }
    }

    fun uploadImage(data: Uri) {
        viewModelScope.launch {
            isImageUploading = true
            userRepository.uploadAvatarImage(data)
            isImageUploading = false
        }
    }

}