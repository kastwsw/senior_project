package io.r3chain.feature.inventory.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.core.data.vo.WasteDocEntity
import io.r3chain.core.data.vo.WasteEntity
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DetailsViewModel.ViewModelFactory::class)
class DetailsViewModel @AssistedInject constructor(
    @Assisted
    private val entity: WasteEntity
) : ViewModel() {

    @AssistedFactory
    interface ViewModelFactory {
        fun create(entity: WasteEntity): DetailsViewModel
    }

    /**
     * Индикатор загрузки.
     */
    var isLoading by mutableStateOf(false)
        private set

    /**
     * Данные записи.
     */
    var wasteData by mutableStateOf(entity)
        private set

    /**
     * Текущий документ верификации.
     */
    var verificationData: WasteDocEntity? by mutableStateOf(null)
        private set


    init {
        viewModelScope.launch {
            isLoading = true
            // TODO: обновить данные, пришедшие из списка
//            data = wasteRepository.getWaste()
            isLoading = false
        }
    }

    /**
     * Изменить значения текущего документа верификации.
     *
     * @param value Основной объект данных.
     */
    fun changeVerificationData(value: WasteDocEntity) {
        // обновить данные формы верификации
        verificationData = value
    }

}