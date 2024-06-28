package io.r3chain.feature.auth

import androidx.compose.runtime.Composable
import io.r3chain.feature.auth.ui.LoginScreen
import javax.inject.Inject

class AuthScreenImpl @Inject constructor() : AuthScreen {

    @Composable
    override fun Draw() {
        LoginScreen()
    }
}