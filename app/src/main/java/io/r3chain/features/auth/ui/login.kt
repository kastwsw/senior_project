package io.r3chain.features.auth.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.R
import io.r3chain.features.auth.model.AuthViewModel
import io.r3chain.ui.components.CheckboxLabel
import io.r3chain.ui.components.PrimaryButton
import io.r3chain.ui.components.TextInput
import io.r3chain.ui.utils.PasswordDelayVisualTransformation
import io.r3chain.ui.utils.clickableLabel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = hiltViewModel()
) {
    Surface {
        Box(
            modifier = Modifier.fillMaxSize(),
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
                        .width(420.dp)
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

                    // контроль видимости
                    val bringRequester = remember {
                        BringIntoViewRequester()
                    }
                    // управление фокусом
                    val focusManager = LocalFocusManager.current
                    // старт авторизации
                    val loginAction = remember(focusManager) {{
                        focusManager.clearFocus(true)
                        authViewModel.signIn()
                    }}

                    // inputs
                    Column(
                        modifier = Modifier
                            .bringIntoViewRequester(bringRequester)
                    ) {
                        // login
                        LoginField(
                            value = authViewModel.login,
                            bringIntoViewRequester = bringRequester
                        ) {
                            authViewModel.changeLogin(it)
                        }
                        Spacer(Modifier.height(28.dp))

                        // password
                        PasswordField(
                            value = authViewModel.password,
                            bringIntoViewRequester = bringRequester,
                            onDoneAction = loginAction
                        ) {
                            authViewModel.changePassword(it)
                        }
                        Spacer(Modifier.height(12.dp))

                        // remember me
                        CheckboxLabel(
                            checked = authViewModel.isRemember.collectAsState(true).value,
                            label = stringResource(R.string.input_remember_label)
                        ) {
                            authViewModel.changeIsRemember(it)
                        }
                        Spacer(Modifier.height(40.dp))

                        // buttons
                        PrimaryButton(
                            text = stringResource(R.string.sign_in_label),
                            enabled = authViewModel.isFormValid && !authViewModel.isLoading,
                            modifier = Modifier.fillMaxWidth(),
                            onClick = authViewModel::signIn
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(
                                24.dp,
                                Alignment.CenterHorizontally
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.sign_up_hint),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            val context = LocalContext.current
                            Text(
                                text = stringResource(R.string.sign_up_label),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.clickableLabel {
                                    authViewModel.signUp(context)
                                }
                            )
                        }
                    }
                }
            }

            // loading indicator
            if (authViewModel.isLoading) LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LoginField(
    value: String,
    bringIntoViewRequester: BringIntoViewRequester,
    onValueChange: (String) -> Unit
) {
    TextInput(
        value = value,
        maxLength = 64,
        modifier = Modifier.fillMaxWidth(),
        labelValue = stringResource(R.string.input_email_label),
        placeholderValue = stringResource(R.string.input_email_placeholder),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            autoCorrect = true,
            imeAction = ImeAction.Next
        ),
        bringIntoViewRequester = bringIntoViewRequester,
        onValueChange = onValueChange
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PasswordField(
    value: String,
    bringIntoViewRequester: BringIntoViewRequester,
    onDoneAction: () -> Unit,
    onValueChange: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var jobTyping: Job? by remember {
        mutableStateOf(null)
    }
    DisposableEffect(Unit) {
        onDispose {
            jobTyping?.cancel()
        }
    }

    // печатает юзер или нет
    var isTyping by remember {
        mutableStateOf(false)
    }
    // скрыт пароль или нет
    var isHidden by remember {
        mutableStateOf(true)
    }
    // текущая трансформация текста в поле ввода
    val currentTransformation by remember(isHidden, isTyping) {
        derivedStateOf {
            when {
                !isHidden -> VisualTransformation.None
                isTyping -> PasswordDelayVisualTransformation()
                else -> PasswordVisualTransformation()
            }
        }
    }

    TextInput(
        value = value,
        maxLength = 64,
        modifier = Modifier.fillMaxWidth(),
        labelValue = stringResource(R.string.input_password_label),
        trailingPainter = painterResource(
            if (isHidden) R.drawable.ic_hide
            else R.drawable.ic_unhide
        ),
        trailingOnClick = {
            isHidden = !isHidden
        },
        visualTransformation = currentTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            autoCorrect = true,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onAny = {
                onDoneAction()
            }
        ),
        bringIntoViewRequester = bringIntoViewRequester,
        onValueChange = {
            // юзер печатает, если был добавлен 1 символ
            isTyping = it.length - value.length == 1
            // отменить предыдущую задачу
            jobTyping?.cancel()
            if (isTyping) {
                // через 1 секунду считать что всё
                jobTyping = coroutineScope.launch {
                    delay(700)
                    isTyping = false
                }
            }
            // отработать
            onValueChange(it)
        }
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Preview(
    name = "Inputs"
)
@Composable
private fun InputPreview() {
    val bringRequester = remember { BringIntoViewRequester() }
    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier
                    .bringIntoViewRequester(bringRequester)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                LoginField(
                    value = "",
                    bringIntoViewRequester = bringRequester
                ) {}
                Spacer(Modifier.height(32.dp))
                PasswordField(
                    value = "somepass",
                    bringIntoViewRequester = bringRequester,
                    onDoneAction = {}
                ) {}
                Spacer(Modifier.height(16.dp))
                // remember me
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = true,
                        onCheckedChange = {}
                    )
                    Text(
                        text = stringResource(R.string.input_remember_label),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}