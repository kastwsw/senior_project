package io.r3chain.feature.auth.model

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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var userRepository: UserRepository
    private lateinit var viewModel: AuthViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        userRepository = mock()
        whenever(userRepository.getRememberMeFlow())
            .thenReturn(flowOf(false))
        viewModel = AuthViewModel(userRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testInitialState() {
        assertEquals("test3@example.com", viewModel.login)
        assertEquals("test_pass", viewModel.password)
        assertFalse(viewModel.isLoading)
        assertTrue(viewModel.isFormValid)
    }

    @Test
    fun testChangeLogin() {
        viewModel.changeLogin("new_login@example.com")
        assertEquals("new_login@example.com", viewModel.login)
        assertTrue(viewModel.isFormValid)
    }

    @Test
    fun testChangePassword() {
        viewModel.changePassword("new_password")
        assertEquals("new_password", viewModel.password)
        assertTrue(viewModel.isFormValid)
    }

    @Test
    fun testFormValidation() {
        viewModel.changeLogin("")
        assertFalse(viewModel.isFormValid)

        viewModel.changeLogin("test@example.com")
        assertTrue(viewModel.isFormValid)

        viewModel.changePassword("")
        assertFalse(viewModel.isFormValid)

        viewModel.changePassword("password")
        assertTrue(viewModel.isFormValid)
    }

    @Test
    fun testChangeIsRemember() = runTest {
        viewModel.changeIsRemember(true)
        verify(userRepository).saveRememberMe(true)
    }

    @Test
    fun testSignIn() = runTest {
        whenever(userRepository.authUser("test3@example.com", "test_pass"))
            .thenReturn(Unit)

        viewModel.signIn()

        // продвинуть выполнение корутин
        advanceUntilIdle()

        verify(userRepository).authUser("test3@example.com", "test_pass")
        assertFalse(viewModel.isLoading)
    }
}
