package io.r3chain.features.root.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import io.r3chain.features.auth.ui.LoginScreen
import io.r3chain.features.root.model.RootModel
import io.r3chain.navigation.SharedModel
import io.r3chain.ui.theme.R3Theme

val LocalSharedModel = compositionLocalOf<SharedModel> {
    error("No instance in composition.")
}

@Composable
fun App() {
    R3Theme {
        CompositionLocalProvider(
            LocalSharedModel provides viewModel<RootModel>()
        ) {
            LoginScreen()
        }
    }
}

@Preview(name = "Login", showSystemUi = true)
@Composable
private fun ScreenPreview() {
    App()
}