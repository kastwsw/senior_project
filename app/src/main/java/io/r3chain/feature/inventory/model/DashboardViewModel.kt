package io.r3chain.feature.inventory.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.core.data.repositories.UserRepository
import io.r3chain.core.data.repositories.WasteMockRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val wasteRepository: WasteMockRepository
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

    /**
     * Общий вес.
     */
    var totalWeight by mutableIntStateOf(0)
        private set


    // NOTE: пока мок в памяти
    val inventoryList = wasteRepository.getInventory()

    // NOTE: пока мок в памяти
    val dispatchedList = wasteRepository.getDispatched()


    init {
        viewModelScope.launch {
            isLoading = true
            userRepository.refresh()
//            wasteRepository.getInventory()
            isLoading = false
        }
    }

}