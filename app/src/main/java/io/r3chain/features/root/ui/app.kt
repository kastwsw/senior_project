package io.r3chain.features.root.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.r3chain.features.root.model.RootModel
import io.r3chain.ui.theme.R3Theme

@Composable
fun App() {
    R3Theme {
        Content()
    }
}

@Composable
private fun Content(
    model: RootModel = hiltViewModel()
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
            startDestination = model.currentScreen.name
        ) {
            composable(route = RootModel.ScreenState.LOADING.name) {
                RootScreen.Loading.Draw()
            }
            composable(route = RootModel.ScreenState.AUTH.name) {
                RootScreen.Auth.Draw()
            }
            composable(route = RootModel.ScreenState.INSIDE.name) {
                RootScreen.Sets.Draw()
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
