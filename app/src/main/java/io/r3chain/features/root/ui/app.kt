package io.r3chain.features.root.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import io.r3chain.navigation.NavigationModel
import io.r3chain.ui.theme.R3Theme

@Composable
fun App() {
    R3Theme {
        Content()
    }
}

@Composable
private fun Content(
    model: NavigationModel = viewModel()
) {
    if (model.currentUser == null) {
        RootScreen.Auth.Draw()
    } else {
        RootScreen.Sets.Draw()
    }

    LaunchedEffect(Unit) {
        model.initUser()
    }
}

@Preview(name = "Login", showSystemUi = true)
@Composable
private fun ScreenPreview() {
    App()
}