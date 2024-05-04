package io.r3chain.features.auth.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import io.r3chain.features.auth.presentation.LocalModel
import io.r3chain.features.auth.presentation.Model
import io.r3chain.ui.theme.R3Theme

@Composable
fun App() {
    R3Theme {
        CompositionLocalProvider(
            LocalModel provides viewModel<Model>()
        ) {
            AuthScreen()
        }
    }
}

@Preview(name = "Login", showSystemUi = true)
@Composable
private fun ScreenPreview() {
    App()
}