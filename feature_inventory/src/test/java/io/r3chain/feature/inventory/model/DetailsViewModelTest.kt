package io.r3chain.feature.inventory.model

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.r3chain.core.data.vo.WasteDocEntity
import io.r3chain.core.data.vo.WasteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var viewModel: DetailsViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val testWasteEntity = WasteEntity(id = 1)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DetailsViewModel(testWasteEntity)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() = runTest {
        assertEquals(testWasteEntity, viewModel.wasteData)
        advanceUntilIdle()
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun testChangeVerificationData() = runTest {
        val newDoc = WasteDocEntity(id = 1, vehicleNumber = "x999xx99")
        viewModel.changeVerificationData(newDoc)
        assertEquals(newDoc, viewModel.verificationData)
    }
}
