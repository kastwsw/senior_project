package io.r3chain.core.data.repositories

import io.r3chain.core.data.vo.WasteEntity
import kotlinx.coroutines.flow.Flow

interface WasteRepository {

    /**
     * Получает все inventory-записи пользователя.
     */
    fun getInventory(): Flow<List<WasteEntity>>

    /**
     * Получает все dispatch-записи пользователя.
     */
    fun getDispatched(): Flow<List<WasteEntity>>

    /**
     * Добавляет новую запись отходов.
     */
    suspend fun addWaste(value: WasteEntity): WasteEntity

    /**
     * Обновляет запись отходов.
     */
    suspend fun updateWaste(value: WasteEntity): WasteEntity

    /**
     * Удаляет запись отходов.
     */
    suspend fun removeWaste(value: WasteEntity)
}
