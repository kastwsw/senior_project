package io.r3chain.features.root.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
            .navigationBarsPadding()
    ) {
        val navigationController = rememberNavController()

        // navigation sets
        NavHost(
            navController = navigationController,
            startDestination = RootScreen.Loading.javaClass.name
        ) {
            composable(route = RootScreen.Loading.javaClass.name) {
                RootScreen.Loading.Draw()
            }
            composable(route = RootScreen.Auth.javaClass.name) {
                RootScreen.Auth.Draw()
            }
            composable(route = RootScreen.Sets.javaClass.name) {
                RootScreen.Sets.Draw()
            }
        }

        // navigate by state
        LaunchedEffect(model.currentUser) {
            val route = navigationController.currentBackStackEntry?.destination?.route
            if (model.currentUser == null) {
                if (route != RootScreen.Auth.javaClass.name) navigationController.navigate(
                    RootScreen.Auth.javaClass.name
                )
            } else {
                if  (route != RootScreen.Sets.javaClass.name) navigationController.navigate(
                    RootScreen.Sets.javaClass.name
                )
            }
        }

//        // есть или нет коннект
//        if (!model.hasConnection) ErrorPlate(
//            text = stringResource(R.string.error_no_connection),
//            modifier = Modifier.align(Alignment.BottomCenter)
//        )
    }
}
