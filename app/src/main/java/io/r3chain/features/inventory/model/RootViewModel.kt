package io.r3chain.features.inventory.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.repositories.WasteMockRepository
import io.r3chain.data.vo.WasteVO
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class RootViewModel @Inject constructor(
    private val wasteRepository: WasteMockRepository
) : ViewModel() {

    /**
     * Запись-намерение расшаренная по UI.
     */
    var intentWaste: WasteVO? = null
        private set

    /**
     * Текущий объект навигации.
     */
    var navController by mutableStateOf<NavHostController?>(null)


    fun navigateBack() {
        intentWaste = null
        navController?.navigateUp()
    }

    fun navigateToProfile() {
        navController?.navigate(ScreenState.PROFILE.name) {
            launchSingleTop = true
        }
    }

    fun navigateToAddCollect() {
        navController?.navigate(ScreenState.COLLECT.name) {
            launchSingleTop = true
        }
    }

    fun navigateToCollectDoc() {
        navController?.navigate(ScreenState.COLLECT.name + ScreenStateWaste.DOC.name) {
            launchSingleTop = true
        }
    }

    fun navigateToAddReceive() {
        navController?.navigate(ScreenState.RECEIVE.name) {
            launchSingleTop = true
        }
    }

    fun navigateToReceiveDoc() {
        navController?.navigate(ScreenState.RECEIVE.name + ScreenStateWaste.DOC.name) {
            launchSingleTop = true
        }
    }

    fun navigateToAddDispatch() {
        navController?.navigate(ScreenState.DISPATCH.name) {
            launchSingleTop = true
        }
    }

    fun navigateToDispatchDoc() {
        navController?.navigate(ScreenState.DISPATCH.name + ScreenStateWaste.DOC.name) {
            launchSingleTop = true
        }
    }

    fun navigateToWasteDetails(record: WasteVO) {
        intentWaste = record
        navController?.navigate(ScreenState.DETAILS.name) {
            launchSingleTop = true
        }
    }

    fun navigateToWasteEdit(record: WasteVO) {
        intentWaste = record
        navController?.navigate(ScreenState.EDIT.name) {
            launchSingleTop = true
        }
    }


    private val _newRecords = MutableSharedFlow<WasteVO>(
        // 0 - новые подписчики не получат предыдущие ошибки
        replay = 0,
        // хранить не более 10 ошибок
        extraBufferCapacity = 10,
        // удалять старые ошибоки в случае переполнения буфера
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    /**
     * Поток добавленных записей.
     */
    val newRecords = _newRecords.asSharedFlow()

    fun recordAdded(data: WasteVO) {
        navigateBack()
        viewModelScope.launch {
            _newRecords.emit(data)
        }
    }

    fun undoRecordAdded(data: WasteVO) {
        viewModelScope.launch {
            wasteRepository.removeWaste(data)
        }
    }

    fun deleteRecord(data: WasteVO) {
        navigateBack()
        viewModelScope.launch {
            wasteRepository.removeWaste(data)
        }
    }


    enum class ScreenState {
        HOME, PROFILE, COLLECT, RECEIVE, DISPATCH, DETAILS, EDIT
    }

    enum class ScreenStateWaste {
        FORM, DOC
    }

}