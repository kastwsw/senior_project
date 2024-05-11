package io.r3chain.features.auth.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.r3chain.R
import io.r3chain.features.auth.model.AuthModel
import io.r3chain.navigation.NavigationModel
import io.r3chain.ui.atoms.ErrorPlate
import io.r3chain.ui.atoms.PrimaryButton
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    authModel: AuthModel = viewModel()
) {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                contentAlignment = Alignment.Center
            ) {
                // content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(26.dp)
                ) {
                    // logo & slogan
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = null,
                        modifier = Modifier.height(52.dp)
                    )
                    Spacer(Modifier.height(22.dp))
                    Text(
                        text = stringResource(R.string.r3_slogan),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(Modifier.height(56.dp))

                    // inputs
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                shape = MaterialTheme.shapes.medium
                            )
                    )
                    Spacer(Modifier.height(40.dp))

                    // buttons
                    PrimaryButton(
                        text = stringResource(R.string.sign_in_label),
                        enabled = !authModel.isLoading,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        authModel.signIn(
                            email = "test3@example.com",
                            password = "test_pass"
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.sign_up_hint),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = stringResource(R.string.sign_up_label)
                        )
                    }
                }
            }

            // loading indicator
            if (authModel.isLoading) LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )

            // errrors
            AppErrors(modifier = Modifier.padding(16.dp))
        }
    }
}

/**
 * Ошибки доступа к серверу.
 */
@Composable
private fun AppErrors(
    modifier: Modifier = Modifier,
    model: NavigationModel = viewModel()
) {
    var showError by remember {
        mutableStateOf(false)
    }

    AnimatedVisibility(
        visible = showError,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        ErrorPlate(
            text = stringResource(R.string.error_no_server),
            modifier = modifier
        )
    }

    LaunchedEffect(model.apiErrors) {
        model.apiErrors.collect {
            showError = true
            delay(3500)  // Показываем сообщение в течение 2 секунд
            showError = false
        }
    }
}