package io.r3chain.feature.auth

import androidx.compose.runtime.Composable
import io.r3chain.feature.auth.ui.LoginScreen

class AuthScreenImpl() : AuthScreen {

    @Composable
    override fun Draw() {
        LoginScreen()
    }
}