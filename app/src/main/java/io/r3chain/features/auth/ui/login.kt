package io.r3chain.features.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.r3chain.R
import io.r3chain.features.root.ui.LocalSharedModel
import io.r3chain.ui.atoms.PrimaryButton
import kotlinx.coroutines.launch

@Composable
fun LoginScreen() {
    val presenter = LocalSharedModel.current
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
            var isLoading by remember {
                mutableStateOf(false)
            }
            Text(
                text = presenter.currentUser?.firstName ?: ""
            )
            val coroutineScope = rememberCoroutineScope()
            PrimaryButton(
                text = stringResource(R.string.action_sign_in),
                enabled = !isLoading
            ) {
                coroutineScope.launch {
                    isLoading = true
                    presenter.signIn(
                        email = "test3@example.com",
                        password = "test_pass"
                    )
                    isLoading = false
                }
            }
        }
    }
}
