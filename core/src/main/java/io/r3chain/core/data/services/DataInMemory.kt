package io.r3chain.core.data.services

import io.r3chain.core.data.vo.WasteVO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataInMemory @Inject constructor() {

    private val _inventory = MutableStateFlow(emptyList<WasteVO>())
    val inventory = _inventory.asStateFlow()

    private val _dispatched = MutableStateFlow(emptyList<WasteVO>())
    val dispatched = _dispatched.asStateFlow()


    suspend fun addToInventory(value: WasteVO): WasteVO {
        val newValue = value.copy(
            id = (_inventory.value.maxOfOrNull { it.id } ?: 0) + 1
        )
        _inventory.emit(_inventory.value + listOf(newValue))
        return newValue
    }

    suspend fun removeFromInventory(value: WasteVO) {
        _inventory.emit(
            _inventory.value.filter {
                it.id != value.id
            }
        )
    }


    suspend fun addToDispatched(value: WasteVO): WasteVO {
        val newValue = value.copy(
            id = (_dispatched.value.maxOfOrNull { it.id } ?: 0) + 1
        )
        _dispatched.emit(_dispatched.value + listOf(newValue))
        return newValue
    }

    suspend fun removeFromDispatched(value: WasteVO) {
        _dispatched.emit(
            _dispatched.value.filter {
                it.id != value.id
            }
        )
    }

}