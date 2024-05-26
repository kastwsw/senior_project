package io.r3chain.features.inside.model

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

    /**
     * Индикатор загрузки.
     */
    var isLoading by mutableStateOf(false)
        private set


    var uri: Uri? by mutableStateOf(null)


    init {
        // отслеживать данные текущего пользователя
        viewModelScope.launch {
            userRepository.getUserFlow().collectLatest {
                currentUser = it
            }
        }

        // отслеживать данные картинки аватара
        viewModelScope.launch {
            userRepository.getPictureFlow().collectLatest { data ->
                uri = data.posterLink.takeIf {
                    it.isNotBlank()
                }?.let {
                    Uri.parse(it)
                }
            }
        }
    }

    fun refreshUserData() {
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

    fun openHelp(context: Context) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(context.getString(R.string.help_link))
            )
        )
    }

    fun openSupport(context: Context) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(context.getString(R.string.support_link))
            )
        )
    }

    fun uploadImage(data: Uri) {
        viewModelScope.launch {
            userRepository.uploadAvatarImage(data)
        }
    }

}