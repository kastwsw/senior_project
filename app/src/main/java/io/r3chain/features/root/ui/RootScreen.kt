package io.r3chain.features.root.ui

import androidx.compose.runtime.Composable
import io.r3chain.features.auth.ui.LoginScreen
import io.r3chain.features.profile.ui.SettingsScreen
import io.r3chain.navigation.IScreen

/**
 * Базовый клас для всех объектов экранов.
 */
sealed class RootScreen : IScreen {

    data object Loading : RootScreen() {
        @Composable
        override fun Draw() {
            Loading()
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