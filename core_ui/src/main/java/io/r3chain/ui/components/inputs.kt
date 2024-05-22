package io.r3chain.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * Базовый элемент для полей ввода.
 *
 * @param value Текст, который будет показан на старте.
 * @param modifier Модификатор.
 * @param enabled Активен или нет.
 * @param labelValue Строка текста подсказки. Может использоваться и для ошибки.
 * @param placeholderValue Строка текста плейсхолдера (когда поле пустое).
 * @param leadingPainter the optional leading painter to be displayed at the beginning.
 * @param trailingPainter the optional trailing painter to be displayed at the end.
 * @param trailingOnClick Действие по клику на trailing icon.
 * @param isError Ошибка или нет.
 * @param singleLine В одну строку или нет.
 * @param maxLines Максимальное количество видимых строк ввода.
 * @param maxLength Ограничение количества символов ввода.
 * @param visualTransformation transforms the visual representation of the input [value]
 * For example, you can use
 * [PasswordVisualTransformation][androidx.compose.ui.text.input.PasswordVisualTransformation] to
 * create a password text field. By default no visual transformation is applied
 * @param keyboardOptions software keyboard options that contains configuration such as
 * [KeyboardType] and [ImeAction].
 * @param keyboardActions when the input service emits an IME action, the corresponding callback
 * is called. Note that this IME action may be different from what you specified in
 * [KeyboardOptions.imeAction].
 * @param bringIntoViewRequester Объект прикрепленный к зоне компоновки,
 * которая должна быть видима при работе с этим полем ввода.
 * @param onValueChange Обратный вызов по вводу.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TextInput(
    value: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    labelValue: String? = null,
    placeholderValue: String? = null,
    leadingPainter: Painter? = null,
    trailingPainter: Painter? = null,
    trailingOnClick: (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = true,
    maxLines: Int = 1,
    maxLength: Int = Int.MAX_VALUE,
    bringIntoViewRequester: BringIntoViewRequester? = null,
    onValueChange: (String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val labelSlot: (@Composable () -> Unit)? = remember(labelValue) {
        labelValue?.let {
            @Composable {
                Text(text = it)
            }
        }
    }

    val placeholderSlot: (@Composable () -> Unit)? = remember(placeholderValue) {
        placeholderValue?.let {
            @Composable {
                CompositionLocalProvider(
                    LocalContentColor provides MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.62f
                    )
                ) {
                    Text(text = it)
                }
            }
        }
    }

    val leadingIconSlot: (@Composable () -> Unit)? = remember(leadingPainter) {
        leadingPainter?.let {
            @Composable {
                Icon(
                    painter = it,
                    contentDescription = null
                )
            }
        }
    }

    val trailingIconSlot: (@Composable () -> Unit)? = remember(trailingPainter) {
        trailingPainter?.let {
            @Composable {
                IconButton(
                    onClick = {
                        trailingOnClick?.invoke()
                    }
                ) {
                    Icon(
                        painter = it,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = 0.62f
                        )
                    )
                }
            }
        }
    }

    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= maxLength) {
                onValueChange(it)
            }
        },
        modifier = Modifier
            .then(modifier)
            // если нужно сместиться к определённому вью, то дополнить модификатор
            .let {
                if (bringIntoViewRequester == null) it
                else it.onFocusEvent { focusState ->
                    if (focusState.isFocused) coroutineScope.launch {
                        // задержка с поправкой на изменение компоновки под клавиатуру
                        delay(500L)
                        bringIntoViewRequester.bringIntoView()
                    }
                }
            },
        enabled = enabled,
        label = labelSlot,
        placeholder = placeholderSlot,
        leadingIcon = leadingIconSlot,
        trailingIcon = trailingIconSlot,
        visualTransformation = visualTransformation,
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        shape = MaterialTheme.shapes.small,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
        )
    )
}


/**
 * Checkboxes allow users to select one or more items from a set. Checkboxes can turn an option on
 * or off.

 * @param checked whether Checkbox is checked or unchecked
 * @param modifier Modifier to be applied to the layout of the checkbox
 * @param label a text near the Checkbox
 * @param enabled whether the component is enabled or grayed out
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 * [Interaction]s for this Checkbox. You can create and pass in your own remembered
 * [MutableInteractionSource] if you want to observe [Interaction]s and customize the
 * appearance / behavior of this Checkbox in different [Interaction]s.
 * @param onCheckedChange callback to be invoked when checkbox is being clicked,
 * therefore the change of checked state in requested.  If null, then this is passive
 * and relies entirely on a higher-level component to control the "checked" state.
 */
@Composable
fun CheckboxLabel(
    checked: Boolean,
    modifier: Modifier = Modifier,
    label: String? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .then(modifier)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClickLabel = label,
                role = Role.Checkbox,
                onClick = {
                    onCheckedChange(!checked)
                }
            )
    ) {
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 44.dp)
            )
        }
        Checkbox(
            checked = checked,
            enabled = enabled,
            interactionSource = interactionSource,
            onCheckedChange = onCheckedChange
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview(
    name = "Demo"
)
@Composable
private fun InputsPreview() {
    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // email
                TextInput(
                    value = "",
                    modifier = Modifier.fillMaxWidth(),
                    labelValue = "Email",
                    placeholderValue = "your@mail.cc",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        autoCorrect = true,
                        imeAction = ImeAction.Next
                    )
                ) {}
                // pass
                TextInput(
                    value = "",
                    modifier = Modifier.fillMaxWidth(),
                    labelValue = "Password",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        autoCorrect = true,
                        imeAction = ImeAction.Done
                    )
                ) {}
                // checkbox
                CheckboxLabel(
                    checked = true,
                    label = "Select me"
                ) {}
            }
        }
    }
}
