package io.r3chain.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import io.r3chain.presentation.LocalPresenter
import io.r3chain.presentation.Presenter
import io.r3chain.ui.theme.R3Theme

@Composable
fun App() {
    R3Theme {
        CompositionLocalProvider(
            LocalPresenter provides viewModel<Presenter>()
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