package io.r3chain.data.repositories

import io.r3chain.data.services.DataInMemory
import io.r3chain.data.vo.WasteVO
import javax.inject.Inject

class WasteMockRepository @Inject constructor(
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


    suspend fun addWaste(value: WasteVO): WasteVO {
        // TODO: по параметрам value определить куда её передавать
//        service.addToDispatched(value)
        return service.addToInventory(value)
    }

    suspend fun removeWaste(value: WasteVO) {
        // TODO: по параметрам value определить откуда её убирать
        service.removeFromInventory(value)
    }

}