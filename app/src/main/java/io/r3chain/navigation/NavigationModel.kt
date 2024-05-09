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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
open class NavigationModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val connectivityService: ConnectivityService,
    private val userRepository: UserRepository
) : ViewModel() {

    var hasConnection by mutableStateOf(true)
        private set

    init {
        // Доступ в Интернет
        viewModelScope.launch(Dispatchers.IO) {
            // не реагировать не мелкие изменения состояния
            @OptIn(FlowPreview::class)
            connectivityService.internetAvailableFlow
                .debounce(800)
                .distinctUntilChanged()
                .collect {
                    withContext(Dispatchers.Main) {
                        hasConnection = it
                    }
                }
        }

        // Начать отслеживать данные текущего пользователя.
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getUserFlow().collectLatest {
                withContext(Dispatchers.Main) {
                    currentUser = it
                }
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