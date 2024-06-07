package io.r3chain.features.inventory.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.r3chain.R
import io.r3chain.data.vo.WasteType

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
