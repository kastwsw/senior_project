package io.r3chain.feature.inventory.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.core.data.repositories.WasteRepository
import io.r3chain.core.data.vo.WasteEntity
import io.r3chain.core.data.vo.WasteRecordType
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
    private val wasteRepository: WasteRepository
) : ViewModel() {

    /**
     * Запись-намерение расшаренная по UI.
     */
    var intentWaste: WasteEntity? = null
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

    fun navigateToWasteEdit(record: WasteEntity) {
        intentWaste = record
        navController?.navigate(ScreenState.EDIT.name) {
            launchSingleTop = true
        }
    }

    fun navigateToAddCollect() {
        navigateToWasteEdit(WasteEntity(recordType = WasteRecordType.COLLECT))
    }

    fun navigateToAddReceive() {
        navigateToWasteEdit(WasteEntity(recordType = WasteRecordType.RECEIVE))
    }

    fun navigateToAddDispatch() {
        navigateToWasteEdit(WasteEntity(recordType = WasteRecordType.DISPATCH))
    }

    fun navigateToWasteEditDocs() {
        navController?.navigate(ScreenState.EDIT.name + ScreenStateWaste.DOC.name) {
            launchSingleTop = true
        }
    }

    fun navigateToWasteDetails(record: WasteEntity) {
        intentWaste = record
        navController?.navigate(ScreenState.DETAILS.name) {
            launchSingleTop = true
        }
    }

    fun navigateToWasteDetailsDocs() {
        navController?.navigate(ScreenState.DETAILS.name + ScreenStateWaste.DOC.name) {
            launchSingleTop = true
        }
    }


    private val _newRecords = MutableSharedFlow<WasteEntity>(
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

    fun recordAdded(data: WasteEntity) {
        navigateBack()
        viewModelScope.launch {
            _newRecords.emit(data)
        }
    }

    fun undoRecordAdded(data: WasteEntity) {
        viewModelScope.launch {
            wasteRepository.removeWaste(data)
        }
    }

    fun deleteRecord(data: WasteEntity) {
        navigateBack()
        viewModelScope.launch {
            wasteRepository.removeWaste(data)
        }
    }


    enum class ScreenState {
        HOME, PROFILE, EDIT, DETAILS
    }

    enum class ScreenStateWaste {
        RECORD, DOC
    }

}