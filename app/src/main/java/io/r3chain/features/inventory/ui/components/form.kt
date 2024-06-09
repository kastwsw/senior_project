package io.r3chain.features.inventory.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.r3chain.R
import io.r3chain.data.vo.WasteType
import io.r3chain.ui.components.IntegerInput
import io.r3chain.ui.components.TextInput
import java.text.NumberFormat
import java.text.ParseException
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
 * Поле ввода веса.
 *
 * @param grams Исходное значение в граммах.
 * @param modifier Модификатор.
 * @param onGramsChange Колбэк полученного значения.
 */
@Composable
fun WeightInput(
    grams: Long?,
    modifier: Modifier = Modifier,
    onGramsChange: (Long?) -> Unit
) {
    fun getKg(weight: Long) = weight / 1000

    fun getGrams(weight: Long) = weight % 1000

    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // kg
        IntegerInput(
            value = grams?.let {
                getKg(it)
            }?.takeIf {
                it != 0L
            }?.toInt(),
            modifier = Modifier.weight(0.62f),
            placeholderValue = stringResource(R.string.inventory_hint_kg),
            maxLength = 6,
            onValueChange = { value ->
                val newKg = value ?: 0
                val g = grams?.let {
                    getGrams(it)
                } ?: 0L
                onGramsChange(newKg * 1000 + g)
            }
        )
        // g
        IntegerInput(
            value = grams?.let {
                getGrams(it)
            }?.takeIf {
                it != 0L
            }?.toInt(),
            modifier = Modifier.weight(0.38f),
            placeholderValue = stringResource(R.string.inventory_hint_g),
            maxLength = 3,
            onValueChange = { value ->
                val newG = value ?: 0
                val kg = grams?.let {
                    getKg(it)
                } ?: 0L
                onGramsChange(kg * 1000 + newG)
            }
        )
    }
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
