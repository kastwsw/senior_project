package io.r3chain.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.r3chain.BuildConfig
import io.r3chain.R
import io.r3chain.data.api.apis.AuthApi
import io.r3chain.data.api.infrastructure.ApiClient
import io.r3chain.data.api.models.AuthLoginRequestDto
import io.r3chain.presentation.theme.R3Theme
import kotlinx.coroutines.launch

@Composable
fun App() {
    R3Theme {
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
                var textResult by remember {
                    mutableStateOf("")
                }
                Text(
                    text = textResult
                )
                val coroutineScope = rememberCoroutineScope()
                FilledTonalButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    onClick = {
                        coroutineScope.launch {
                            val response = ApiClient(baseUrl = BuildConfig.BASE_URL)
                                .createService(AuthApi::class.java)
                                .apiV1AuthLoginPost(
                                    AuthLoginRequestDto(
                                        email = "test3@example.com",
                                        password = "test_pass"
                                    )
                                )
                            textResult = response.body()?.sessionList?.toString() ?: ""
                            println(response.toString())
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.action_sign_in))
                }
            }
        }
    }
}

@Preview(name = "Login", showSystemUi = true)
@Composable
private fun ScreenPreview() {
    App()
}