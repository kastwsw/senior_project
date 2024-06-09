package io.r3chain.features.inventory.ui.components

import android.text.format.DateFormat
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.r3chain.R
import io.r3chain.data.vo.WasteType
import io.r3chain.ui.components.LinkButton
import io.r3chain.ui.components.PrimaryButton
import io.r3chain.ui.components.TextInput
import java.text.NumberFormat
import java.text.ParseException
import java.time.Instant
import java.time.LocalDate
import java.util.Date
import java.util.Locale

@Composable
fun GroupLabel(
    text: String = "",
    paddingValues: PaddingValues = PaddingValues(horizontal = 0.dp, vertical = 12.dp)
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(paddingValues)
    )
}


@Composable
fun RowLabel(
    text: String = "",
    paddingValues: PaddingValues = PaddingValues(start = 12.dp, bottom = 4.dp)
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(paddingValues)
    )
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WasteTypeSelect(
    types: List<WasteType>,
    onSelect: (List<WasteType>) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.Start),
        verticalArrangement = Arrangement.spacedBy((-2).dp)
    ) {
        WasteType.entries.forEach { type ->
            val labelId = when (type) {
                WasteType.PET -> R.string.inventory_label_materials_type_pet
                WasteType.HDPE -> R.string.inventory_label_materials_type_hdpe
                WasteType.PVC -> R.string.inventory_label_materials_type_pvc
                WasteType.LDPE -> R.string.inventory_label_materials_type_pdpe
                WasteType.PP -> R.string.inventory_label_materials_type_pp
                WasteType.PS -> R.string.inventory_label_materials_type_ps
                WasteType.OTHER -> R.string.inventory_label_materials_type_other
            }
            val isSelected = types.any { type == it }
            FilterChip(
                selected = isSelected,
                label = {
                    Text(text = stringResource(labelId))
                },
                onClick = {
                    onSelect(
                        types.toMutableList().apply {
                            if (isSelected) remove(type) else add(type)
                        }.toList()
                    )
                }
            )
        }
    }
}


/**
 * Поле ввода веса в граммах.
 *
 * @param grams Исходное значение в граммах.
 * @param modifier Модификатор.
 * @param onGramsChange Колбэк полученного значения.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeightGramsInput(
    grams: Long?,
    modifier: Modifier = Modifier,
    onGramsChange: (Long?) -> Unit
) {
    TextInput(
        value = grams?.toString() ?: "",
        modifier = modifier,
        maxLength = 12,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        onValueChange = {
            onGramsChange(it.replace(Regex("[^0-9]"), "").toLongOrNull())
        }
    )
}


/**
 * Поле ввода веса в килограммах.
 *
 * @param kg Исходное значение в граммах.
 * @param modifier Модификатор.
 * @param onKgChange Колбэк полученного значения.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeightKgInput(
    kg: Double?,
    modifier: Modifier = Modifier,
    onKgChange: (Double?) -> Unit
) {
    val formatter = remember {
        NumberFormat.getInstance(Locale.getDefault()).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 2
        }
    }

    // только первых проход зависит от входящего числа,
    // дальше текст соответствует тому, что вводит пользователь
    var text by remember {
        mutableStateOf(if (kg == null) "" else formatter.format(kg))
    }

    TextInput(
        value = text,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Decimal
        ),
        onValueChange = {
            // пользователь видит, то что он вводит
            text = it
            // числовое значение уходит в лямбду
            onKgChange(
                try {
                    formatter.parse(it)
                } catch (e: ParseException) {
                    null
                }?.toDouble()
            )
        }
    )
}


/**
 * Поле ввода даты через соответствующий диалог.
 *
 * @param time Исходное значение в миллесекундах.
 * @param modifier Модификатор.
 * @param onTimeChange Колбэк полученного значения.
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DateInput(
    time: Long?,
    modifier: Modifier = Modifier,
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
                    text = stringResource(io.r3chain.ui.R.string.select_date_done),
                    enabled = confirmEnabled
                ) {
                    hasDialog = false
                    // данные из диалога
                    onTimeChange(datePickerState.selectedDateMillis!!)
                }
            },
            dismissButton = {
                LinkButton(
                    text = stringResource(io.r3chain.ui.R.string.select_date_cancel)
                ) {
                    hasDialog = false
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

