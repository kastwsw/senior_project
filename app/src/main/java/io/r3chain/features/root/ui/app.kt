package io.r3chain.features.root.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
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
import io.r3chain.R
import io.r3chain.data.exceptions.AuthException
import io.r3chain.data.exceptions.NetworkIOException
import io.r3chain.data.exceptions.NoInternetException
import io.r3chain.data.exceptions.SurpriseException
import io.r3chain.features.auth.ui.LoginScreen
import io.r3chain.features.profile.ui.SettingsScreen
import io.r3chain.features.root.model.RootViewModel
import io.r3chain.presentation.ApiViewModel
import io.r3chain.ui.atoms.ErrorPlate
import io.r3chain.ui.atoms.LoadingBox
import io.r3chain.ui.theme.R3Theme
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
            .navigationBarsPadding()
    ) {
        val navigationController = rememberNavController()

        // navigation sets
        NavHost(
            navController = navigationController,
            startDestination = model.currentState.name
        ) {
            composable(route = RootViewModel.ScreenState.LOADING.name) {
                Surface {
                    LoadingBox(modifier = Modifier.fillMaxSize())
                }
            }
            composable(route = RootViewModel.ScreenState.AUTH.name) {
                LoginScreen()
            }
            composable(route = RootViewModel.ScreenState.INSIDE.name) {
                SettingsScreen()
            }
        }

        // TODO: проверить анимацию в NavHost, если она работает без принудительного navigate, то OK
//        // navigate by state
//        LaunchedEffect(model.screenState) {
//            val route = navigationController.currentBackStackEntry?.destination?.route
//            when (model.screenState) {
//                RootModel.ScreenState.LOADING -> na
//            }
//            if (model.currentUser == null) {
//                if (route != RootScreen.Auth.javaClass.name) navigationController.navigate(
//                    RootScreen.Auth.javaClass.name
//                )
//            } else {
//                if  (route != RootScreen.Sets.javaClass.name) navigationController.navigate(
//                    RootScreen.Sets.javaClass.name
//                )
//            }
//        }

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

