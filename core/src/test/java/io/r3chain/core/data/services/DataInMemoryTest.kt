package io.r3chain.core.data.services

import io.r3chain.core.data.vo.WasteEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DataInMemoryTest {

    private lateinit var dataInMemory: DataInMemory

    @Before
    fun setUp() {
        dataInMemory = DataInMemory()
    }

    @Test
    fun testAddToInventory() = runTest {
        val wasteEntity = WasteEntity(id = 0, location = 100)
        val result = dataInMemory.addToInventory(wasteEntity)

        assertEquals(1, result.id)
        assertEquals(1, dataInMemory.inventory.first().size)
        assertEquals(result, dataInMemory.inventory.first()[0])
    }

    @Test
    fun testUpdateInInventory() = runTest {
        val wasteEntity = WasteEntity(id = 1, location = 100)
        dataInMemory.addToInventory(wasteEntity)

        val updatedEntity = wasteEntity.copy(location = 200)
        val result = dataInMemory.updateInInventory(updatedEntity)

        assertEquals(updatedEntity, result)
        assertEquals(updatedEntity, dataInMemory.inventory.first()[0])
    }

    @Test
    fun testRemoveFromInventory() = runTest {
        val wasteEntity = WasteEntity(id = 1, location = 100)
        dataInMemory.addToInventory(wasteEntity)

        dataInMemory.removeFromInventory(wasteEntity)

        assertTrue(dataInMemory.inventory.first().isEmpty())
    }

    @Test
    fun testAddToDispatched() = runTest {
        val wasteEntity = WasteEntity(id = 0, location = 100)
        val result = dataInMemory.addToDispatched(wasteEntity)

        assertEquals(1, result.id)
        assertEquals(1, dataInMemory.dispatched.first().size)
        assertEquals(result, dataInMemory.dispatched.first()[0])
    }

    @Test
    fun testUpdateInDispatched() = runTest {
        val wasteEntity = WasteEntity(id = 1, location = 100)
        dataInMemory.addToDispatched(wasteEntity)

        val updatedEntity = wasteEntity.copy(location = 200)
        val result = dataInMemory.updateInDispatched(updatedEntity)

        assertEquals(updatedEntity, result)
        assertEquals(updatedEntity, dataInMemory.dispatched.first()[0])
    }

    @Test
    fun testRemoveFromDispatched() = runTest {
        val wasteEntity = WasteEntity(id = 1, location = 100)
        dataInMemory.addToDispatched(wasteEntity)

        dataInMemory.removeFromDispatched(wasteEntity)

        assertTrue(dataInMemory.dispatched.first().isEmpty())
    }
}
