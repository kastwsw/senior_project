package io.r3chain.data.services

import io.r3chain.data.vo.WasteVO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataInMemory @Inject constructor() {

    private val _inventory = MutableStateFlow(emptyList<WasteVO>())
    val inventory = _inventory.asStateFlow()

    private val _dispatched = MutableStateFlow(emptyList<WasteVO>())
    val dispatched = _inventory.asStateFlow()


    suspend fun addToInventory(value: WasteVO) {
        _inventory.emit(
            _inventory.value + listOf(value)
        )
    }


    suspend fun addToDispatched(value: WasteVO) {
        _dispatched.emit(
            _dispatched.value + listOf(value)
        )
    }

}