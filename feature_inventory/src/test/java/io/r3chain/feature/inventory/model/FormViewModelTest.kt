package io.r3chain.feature.inventory.model

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.r3chain.core.data.repositories.ResourcesGateway
import io.r3chain.core.data.repositories.WasteRepository
import io.r3chain.core.data.vo.FileAttachEntity
import io.r3chain.core.data.vo.WasteDocEntity
import io.r3chain.core.data.vo.WasteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.verify
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class FormViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var wasteRepository: WasteRepository
    private lateinit var resourcesGateway: ResourcesGateway
    private lateinit var viewModel: FormViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testWasteEntity = WasteEntity(id = 1)
    private val testEventFlow = MutableSharedFlow<ResourcesGateway.FileEvent>()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        wasteRepository = mock()
        resourcesGateway = mock()

        whenever(resourcesGateway.events)
            .thenReturn(testEventFlow)

        viewModel = FormViewModel(testWasteEntity, wasteRepository, resourcesGateway)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() = runTest {
        assertEquals(testWasteEntity, viewModel.wasteData)
    }

    @Test
    fun testUploadWasteResources() = runTest {
        val uris = listOf(Uri.parse("file://test.jpg"))
        viewModel.uploadWasteResources(uris)
        advanceUntilIdle()

        verify(resourcesGateway).startUploadFile(FileAttachEntity(uri = uris[0], isLoading = true))
        assertTrue(viewModel.wasteData.files.any { it.uri == uris[0] })
    }

    @Test
    fun testDeleteWasteResource() = runTest {
        val file = FileAttachEntity(uri = Uri.parse("file://test.jpg"), isLoading = true)
        viewModel.changeWasteData(
            testWasteEntity.copy(files = listOf(file))
        )

        viewModel.deleteWasteResource(file)
        advanceUntilIdle()

        verify(resourcesGateway).removeFile(file)
        assertFalse(viewModel.wasteData.files.contains(file))
    }

    @Test
    fun testUploadVerificationResources() = runTest {
        val doc = WasteDocEntity(id = 1, vehicleNumber = "x999xx99")
        val uris = listOf(Uri.parse("file://test.jpg"))

        viewModel.uploadVerificationResources(doc, uris)
        advanceUntilIdle()

        assertTrue(viewModel.verificationData?.files?.any { it.uri == uris[0] } ?: false)
    }

    @Test
    fun testDeleteVerificationResource() = runTest {
        val file = FileAttachEntity(uri = Uri.parse("file://test.jpg"), isLoading = true)
        val doc = WasteDocEntity(id = 1, vehicleNumber = "x999xx99", files = listOf(file))

        viewModel.changeVerificationData(doc)
        viewModel.deleteVerificationResource(file)
        advanceUntilIdle()

        assertFalse(viewModel.verificationData?.files?.contains(file) ?: true)
    }

    @Test
    fun testCreateWasteRecord() = runTest {
        viewModel.createWasteRecord()
        advanceUntilIdle()

        verify(wasteRepository).addWaste(testWasteEntity)
        assertTrue(viewModel.doneResult?.isSuccess == true)
    }

    @Test
    fun testUpdateWasteRecord() = runTest {
        viewModel.updateWasteRecord()
        advanceUntilIdle()

        verify(wasteRepository).updateWaste(testWasteEntity)
        assertTrue(viewModel.doneResult?.isSuccess == true)
    }
}
