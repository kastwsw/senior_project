package io.r3chain.feature.inventory.model

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.r3chain.core.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: ProfileViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        userRepository = mock()
        whenever(userRepository.getUserFlow()).thenReturn(flowOf(null))
        whenever(userRepository.getUserExtFlow()).thenReturn(flowOf(null))
        viewModel = ProfileViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() = runTest {
        verify(userRepository).refresh()
        advanceUntilIdle()
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun testSignOut() = runTest {
        viewModel.signOut()
        advanceUntilIdle()
        verify(userRepository).exit()
        assertTrue(viewModel.isLoading)
    }

    @Test
    fun testSetEmailNotification() = runTest {
        viewModel.setEmailNotification(true)
        advanceUntilIdle()
        verify(userRepository).updateUserNotification(enabledEmail = true)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun testUploadImage() = runTest {
        val uri: Uri = mock()
        viewModel.uploadImage(uri)
        advanceUntilIdle()
        verify(userRepository).uploadAvatarImage(uri)
        assertFalse(viewModel.isImageUploading)
    }
}
