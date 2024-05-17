package io.r3chain.features.root.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.r3chain.features.auth.ui.LoginScreen
import io.r3chain.features.profile.ui.SettingsScreen
import io.r3chain.features.root.model.RootViewModel
import io.r3chain.ui.atoms.LoadingBox
import io.r3chain.ui.theme.R3Theme

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

//        // есть или нет коннект
//        if (!model.hasConnection) ErrorPlate(
//            text = stringResource(R.string.error_no_connection),
//            modifier = Modifier.align(Alignment.BottomCenter)
//        )
    }
}
