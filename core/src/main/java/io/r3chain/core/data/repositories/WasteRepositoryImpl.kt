package io.r3chain.core.data.repositories

import io.r3chain.core.data.services.DataInMemory
import io.r3chain.core.data.vo.WasteEntity
import io.r3chain.core.data.vo.WasteRecordType
import javax.inject.Inject

class WasteRepositoryImpl @Inject constructor(
    private val service: DataInMemory
) : WasteRepository {

    /**
     * Получает все inventory-записи пользователя.
     */
    override fun getInventory() = service.inventory

    /**
     * Получает все dispatch-записи пользователя.
     */
    override fun getDispatched() = service.dispatched


    override suspend fun addWaste(value: WasteEntity): WasteEntity {
        return if (value.recordType == WasteRecordType.DISPATCH) {
            service.addToDispatched(value)
        } else {
            service.addToInventory(value)
        }
    }

    override suspend fun updateWaste(value: WasteEntity): WasteEntity {
        return if (value.recordType == WasteRecordType.DISPATCH) {
            service.updateInDispatched(value)
        } else {
            service.updateInInventory(value)
        }
    }

    override suspend fun removeWaste(value: WasteEntity) {
        if (value.recordType == WasteRecordType.DISPATCH) {
            service.removeFromDispatched(value)
        } else {
            service.removeFromInventory(value)
        }
    }

}