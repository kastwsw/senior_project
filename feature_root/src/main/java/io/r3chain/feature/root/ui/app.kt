package io.r3chain.feature.root.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.r3chain.core.data.exceptions.AuthException
import io.r3chain.core.data.exceptions.NetworkIOException
import io.r3chain.core.data.exceptions.NoInternetException
import io.r3chain.core.data.exceptions.SurpriseException
import io.r3chain.feature.auth.ui.LoginScreen
import io.r3chain.feature.inventory.ui.InventoryScreen
import io.r3chain.feature.root.model.RootViewModel
import io.r3chain.feature.root.model.ApiViewModel
import io.r3chain.core.ui.components.ErrorPlate
import io.r3chain.core.ui.components.LoadingBox
import io.r3chain.core.ui.theme.R3Theme
import io.r3chain.feature.root.R
import kotlinx.coroutines.delay

@Composable
fun App() {
    R3Theme {
        Content()
    }
}

@Composable
private fun Content(
    model: RootViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            // NOTE: пока самое простое решение (все отступы здесь)
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
    ) {
        val navigationController = rememberNavController()

        // navigation sets
        NavHost(
            navController = navigationController,
            startDestination = model.currentState.name,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(route = RootViewModel.ScreenState.LOADING.name) {
                Surface {
                    LoadingBox(modifier = Modifier.fillMaxSize())
                }
            }
            composable(route = RootViewModel.ScreenState.AUTH.name) {
                LoginScreen()
            }
            composable(route = RootViewModel.ScreenState.INVENTORY.name) {
                InventoryScreen()
            }
        }

        // errrors
        AppErrors(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter)
        )
    }
}


/**
 * Ошибки доступа к серверу.
 */
@Composable
private fun AppErrors(
    modifier: Modifier = Modifier,
    model: ApiViewModel = hiltViewModel()
) {
    var showError by remember {
        mutableStateOf(false)
    }

    var message by remember {
        mutableStateOf("")
    }

    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = showError,
            enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessMedium)),
            exit = fadeOut(animationSpec = spring(stiffness = Spring.StiffnessMedium))
        ) {
            ErrorPlate(text = message)
        }
    }

    val context = LocalContext.current

    LaunchedEffect(model.apiErrors) {
        model.apiErrors.collect { exception ->
            when (exception) {
                is NoInternetException,
                is NetworkIOException -> context.getString(R.string.error_no_server)
                is AuthException -> context.getString(R.string.error_unauthorized)
                is SurpriseException -> context.getString(R.string.error_surprise)
                else -> null
            }?.also {
                message = it
                showError = true
                // показывать сообщение в течение 3,5 секунд
                delay(3500)
                showError = false
            }
        }
    }
}

