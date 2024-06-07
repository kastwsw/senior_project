package io.r3chain.features.inventory.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.repositories.WasteRepository
import io.r3chain.data.vo.WasteVO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
open class CollectViewModel @Inject constructor(
    private val wasteRepository: WasteRepository
) : ViewModel() {

    /**
     * Индикатор загрузки.
     */
    var isLoading by mutableStateOf(false)
        private set

    /**
     * Данные для формы.
     */
    var data: WasteVO by mutableStateOf(WasteVO())
        private set

    /**
     * Результат обработки формы.
     */
    var doneResult: Result<WasteVO>? by mutableStateOf(null)
        private set


    init {
        changeFormData(
            data.copy(
                time = Instant.now().minus(Duration.ofDays(2)).toEpochMilli()
            )
        )
    }


    /**
     * Отрабатывает подтверждение пользователем заполенния формы.
     */
    fun doneForm() {
        viewModelScope.launch {
            isLoading = true
            delay(500)
//            wasteRepository.addCollect()
//            isLoading = false
            // TODO: передать с успехом новые данные, которые вернул сервер
            doneResult = Result.success(data)
        }
    }

    /**
     * Изменить значение типа материала.
     */
    fun changeFormData(value: WasteVO) {
        // TODO: провалидировать форму (дизейблить/енейблить кнопку send)
        data = value
    }

}