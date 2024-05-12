package io.r3chain.features.root.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.r3chain.features.auth.ui.LoginScreen
import io.r3chain.features.profile.ui.SettingsScreen
import io.r3chain.ui.atoms.LoadingBox

/**
 * Базовый клас для всех объектов экранов.
 */
sealed class RootScreen : IScreen {

    data object Loading : RootScreen() {

        @Composable
        override fun Draw() {
            LoadingBox(modifier = Modifier.fillMaxSize())
        }
    }

    data object Auth : RootScreen() {

        @Composable
        override fun Draw() {
            LoginScreen()
        }
    }

    data object Sets : RootScreen() {

        @Composable
        override fun Draw() {
            SettingsScreen()
        }
    }

}