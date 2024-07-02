package io.r3chain.core.data.repositories

import io.r3chain.core.data.services.DataInMemory
import io.r3chain.core.data.vo.WasteEntity
import io.r3chain.core.data.vo.WasteRecordType
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


    suspend fun addWaste(value: WasteEntity): WasteEntity {
        return if (value.recordType == WasteRecordType.DISPATCH) {
            service.addToDispatched(value)
        } else {
            service.addToInventory(value)
        }
    }

    suspend fun updateWaste(value: WasteEntity): WasteEntity {
        return if (value.recordType == WasteRecordType.DISPATCH) {
            service.updateInDispatched(value)
        } else {
            service.updateInInventory(value)
        }
    }

    suspend fun removeWaste(value: WasteEntity) {
        if (value.recordType == WasteRecordType.DISPATCH) {
            service.removeFromDispatched(value)
        } else {
            service.removeFromInventory(value)
        }
    }

}