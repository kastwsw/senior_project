package io.r3chain.features.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.r3chain.R
import io.r3chain.features.auth.model.AuthModel
import io.r3chain.features.root.ui.LocalSharedModel
import io.r3chain.ui.atoms.PrimaryButton

@Composable
fun LoginScreen(
    authModel: AuthModel = viewModel()
) {
    val sharedModel = LocalSharedModel.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PrimaryButton(
                text = stringResource(R.string.action_sign_in),
                enabled = !authModel.isLoading
            ) {
                authModel.signIn(
                    email = "test3@example.com",
                    password = "test_pass",
                    model = sharedModel
                )
            }
        }
    }
}
