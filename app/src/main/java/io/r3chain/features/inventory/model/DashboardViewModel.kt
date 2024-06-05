package io.r3chain.features.inventory.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.repositories.UserRepository
import io.r3chain.data.repositories.WasteRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class DashboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val wasteRepository: WasteRepository
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
     * Индикатор загрузки.
     */
    var isLoading by mutableStateOf(false)
        private set


    init {
        viewModelScope.launch {
            isLoading = true
            userRepository.refresh()
            wasteRepository.getRecords()
            isLoading = false
        }
    }

}