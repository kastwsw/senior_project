package io.r3chain.core.ui.components

import android.content.res.Configuration
import android.text.format.DateFormat
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.r3chain.core.ui.R
import io.r3chain.core.ui.theme.R3Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.util.Date


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


/**
 * Базовый элемент для полей ввода.
 *
 * @param value Текст, который будет показан на старте.
 * @param modifier Модификатор.
 * @param enabled Активен или нет.
 * @param labelValue Строка текста подсказки. Может использоваться и для ошибки.
 * @param placeholderValue Строка текста плейсхолдера (когда поле пустое).
 * @param leadingVector the optional leading painter to be displayed at the beginning.
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
    leadingVector: ImageVector? = null,
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
    onClick: (() -> Unit)? = null,
    onValueChange: (String) -> Unit
) {
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

    val leadingIconSlot: (@Composable () -> Unit)? = remember(leadingVector) {
        leadingVector?.let {
            @Composable {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    val coroutineScope = bringIntoViewRequester?.let {
        rememberCoroutineScope()
    }

    val focusRequester = remember {
        onClick?.let {
            FocusRequester()
        }
    }

    Box(modifier = Modifier.then(modifier)) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                if (it.length <= maxLength) {
                    onValueChange(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                // если нужно сместиться к определённому вью, то дополнить модификатор
                .let {
                    if (bringIntoViewRequester == null) it else it.onFocusEvent { focusState ->
                        if (focusState.isFocused) coroutineScope?.launch {
                            // задержка с поправкой на изменение компоновки под клавиатуру
                            delay(350L)
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                }
                // если нужна работа с фокусом
                .let {
                    if (focusRequester == null) it else it.focusRequester(focusRequester)
                },
            enabled = enabled,
            readOnly = onClick != null,
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

        // фейк для перехвата клика
        if (onClick != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(OutlinedTextFieldDefaults.shape)
                    .clickable(
                        onClickLabel = placeholderValue,
                        role = Role.Button,
                        enabled = enabled,
                        onClick = {
                            focusRequester?.requestFocus()
                            onClick()
                        }
                    )
            )
        }
    }
}


/**
 * Поле ввода целых чисел.
 *
 * @param value Исходное значение в граммах.
 * @param modifier Модификатор.
 * @param onValueChange Колбэк полученного значения.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntegerInput(
    value: Int?,
    modifier: Modifier = Modifier,
    placeholderValue: String? = null,
    maxLength: Int = 8,
    onValueChange: (Int?) -> Unit
) {
    TextInput(
        value = value?.toString() ?: "",
        modifier = modifier,
        placeholderValue = placeholderValue,
        maxLength = maxLength,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        onValueChange = {
            onValueChange(it.replace(Regex("[^0-9]"), "").toIntOrNull())
        }
    )
}


/**
 * Поле ввода даты через соответствующий диалог.
 *
 * @param time Исходное значение в миллесекундах.
 * @param modifier Модификатор.
 * @param enabled Активен или нет.
 * @param onTimeChange Колбэк полученного значения.
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DateInput(
    time: Long?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onTimeChange: (Long) -> Unit
) {
    var hasDialog by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val formatter = remember(context) {
        DateFormat.getDateFormat(context)
    }

    TextInput(
        value = if (time == null) "" else formatter.format(Date(time)),
        modifier = modifier,
        enabled = enabled,
        leadingVector = Icons.Outlined.Today,
        onClick = {
            hasDialog = true
        },
        onValueChange = {}
    )

    if (hasDialog) {
        val datePickerState = rememberDatePickerState(
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= Instant.now().toEpochMilli()
                }

                override fun isSelectableYear(year: Int): Boolean {
                    return year <= LocalDate.now().year
                }
            }
        )
        val confirmEnabled by remember {
            derivedStateOf {
                datePickerState.selectedDateMillis != null
            }
        }
        DatePickerDialog(
            onDismissRequest = {
                hasDialog = false
            },
            confirmButton = {
                PrimaryButton(
                    text = stringResource(R.string.select_date_done),
                    enabled = confirmEnabled
                ) {
                    hasDialog = false
                    // данные из диалога
                    onTimeChange(datePickerState.selectedDateMillis!!)
                }
            },
            dismissButton = {
                LinkButton(
                    text = stringResource(R.string.select_date_cancel)
                ) {
                    hasDialog = false
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownInput(
    options: List<String>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    onOptionSelect: (Int) -> Unit
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }
    // We want to react on tap/press on TextField to show menu
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.then(modifier)
    ) {
        OutlinedTextField(
            value = options[selectedIndex],
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            shape = MaterialTheme.shapes.small,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            )
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier.exposedDropdownSize(true)
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = {
                        Text(text = option)
                    },
                    onClick = {
                        onOptionSelect(index)
                        expanded = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectableInput(
    options: List<String>,
    modifier: Modifier = Modifier,
    selectedIndex: Int? = null,
    placeholderValue: String? = null,
    onOptionSelect: (Int) -> Unit
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    TextInput(
        value = if (selectedIndex == null) "" else options[selectedIndex],
        modifier = modifier,
        placeholderValue = placeholderValue,
        trailingPainter = painterResource(
            if (expanded) R.drawable.ic_arrow_drop_up
            else R.drawable.ic_arrow_drop_down
        ),
        onClick = {
            expanded = true
        },
        onValueChange = {}
    )

    BottomSelect(
        isVisible = expanded,
        onClose = {
            expanded = false
        },
        onSelect = {
            expanded = false
            onOptionSelect(it)
        }
    ) { optionSelect ->
        options.forEachIndexed { index, option ->
            ActionPlate(title = option) {
                optionSelect(index)
            }
        }
    }
}


////
// PREVIEW
//

@Preview(
    name = "Demo Light",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun PreviewLight() {
    Demo()
}


@Preview(
    name = "Demo Night",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun PreviewNight() {
    Demo()
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Demo() {
    R3Theme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // email
                TextInput(
                    value = "",
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
                    labelValue = "Password",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        autoCorrect = true,
                        imeAction = ImeAction.Done
                    )
                ) {}
                // email
                TextInput(
                    value = "",
                    modifier = Modifier.width(200.dp),
                    leadingVector = Icons.Outlined.Today,
                    labelValue = "Email",
                    placeholderValue = "your@mail.cc",
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        autoCorrect = true,
                        imeAction = ImeAction.Next
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
