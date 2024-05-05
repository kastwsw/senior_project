package io.r3chain.features.root.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
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
            Content()
        }
    }
}

@Composable
private fun Content() {
    val model = LocalSharedModel.current as RootModel
    if (model.currentUser == null) {
        RootScreen.Auth.Draw()
    } else {
        RootScreen.Sets.Draw()
    }
}

@Preview(name = "Login", showSystemUi = true)
@Composable
private fun ScreenPreview() {
    App()
}