package io.r3chain.data.repositories

import io.r3chain.data.services.DataInMemory
import io.r3chain.data.vo.WasteVO
import javax.inject.Inject

class WasteInMemoryRepository @Inject constructor(
    private val service: DataInMemory
) {

    /**
     * Получает все inventory-записи пользователя.
     */
    fun getInventory() = service.inventory

    /**
     * Получает все dispatch-записи пользователя.
     */
    fun getDispatched() = service.dispatched


    suspend fun addWaste(value: WasteVO) {
        // TODO: по параметрам value определить куда её передавать
        service.addToInventory(value)
//        service.addToDispatched(value)
    }

}