package io.r3chain.feature.inventory.ui.components

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import io.r3chain.core.data.vo.FileAttachEntity
import io.r3chain.core.data.vo.WasteDocEntity
import io.r3chain.core.data.vo.WasteDocType
import io.r3chain.core.data.vo.WasteType
import io.r3chain.core.ui.components.ImagesSelect
import io.r3chain.core.ui.components.IntegerInput
import io.r3chain.core.ui.components.TextInput
import io.r3chain.feature.inventory.R
import java.text.NumberFormat
import java.text.ParseException
import java.util.Locale

@Composable
fun GroupLabel(
    text: String = "",
    paddingValues: PaddingValues = PaddingValues(bottom = 12.dp)
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(paddingValues)
    )
}


/**
 * Возвращает id стоки для описания типа документа.
 */
fun getDocTypeStringId(type: WasteDocType) = when (type) {
    WasteDocType.SLIP -> R.string.inventory_verifications_type_slip
    WasteDocType.PHOTO -> R.string.inventory_verifications_type_photo
    WasteDocType.CERT -> R.string.inventory_verifications_type_cert
    WasteDocType.INVOICE -> R.string.inventory_verifications_type_invoice
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
        Column(
            modifier = Modifier.weight(0.62f)
        ) {
            RowLabel(text = stringResource(R.string.inventory_label_weight))
            IntegerInput(
                value = grams?.let {
                    getKg(it)
                }?.takeIf {
                    it != 0L
                }?.toInt(),
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
        }
        // g
        Column(
            modifier = Modifier.weight(0.38f)
        ) {
            RowLabel(text = stringResource(R.string.inventory_label_weight_grams))
            IntegerInput(
                value = grams?.let {
                    getGrams(it)
                }?.takeIf {
                    it != 0L
                }?.toInt(),
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
            maximumFractionDigits = 3
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


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PhotoRow(
    data: List<FileAttachEntity>,
    onUriSelected: (List<Uri>) -> Unit
) {
    var isImageSelectVisible by rememberSaveable {
        mutableStateOf(false)
    }

    // 4 colums grid
    val columnsAmount = 4
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = columnsAmount,
        modifier = Modifier.fillMaxWidth()
    ) {
        // фотки
        data.forEach {
            FileItem(file = it, modifier = Modifier.weight(1f))
        }
        // кнопка добавить
        FileBox(
            modifier = Modifier
                .weight(1f)
                .clickable(
                    onClickLabel = stringResource(R.string.inventory_label_add_image),
                    role = Role.Button,
                    onClick = {
                        isImageSelectVisible = true
                    }
                )
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                modifier = Modifier.size(20.dp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        // добивает для ровной строки
        ((data.size + 1) % columnsAmount).takeIf {
            it != 0
        }?.also {
            repeat(columnsAmount - it) {
                Spacer(Modifier.weight(1f).aspectRatio(1f))
            }
        }
    }

    ImagesSelect(
        isVisible = isImageSelectVisible,
        maxSelect = 8,
        onClose = {
            isImageSelectVisible = false
        },
        onSelect = {
            onUriSelected(it)
        }
    )
}


@Composable
private fun FileBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .then(modifier)
            .aspectRatio(1f)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = shape
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = shape
            )
            .clip(
                shape = shape
            ),
        contentAlignment = Alignment.Center,
        content = content
    )
}


@Composable
private fun FileItem(
    file: FileAttachEntity,
    modifier: Modifier = Modifier
) {
    if (file.isLoading) {
        // загружается
        FileBox(
            modifier = Modifier
                .then(modifier)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(36.dp),
                strokeWidth = 4.dp,
                strokeCap = StrokeCap.Round
            )
        }
    } else {
        // загружено
        file.resource?.also { vo ->
            Image(
                painter = rememberAsyncImagePainter(vo.posterLink),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .then(modifier)
                    .aspectRatio(1f)
                    .clip(shape = RoundedCornerShape(8.dp))
            )
        }
        // TODO: если ошибка
        // TODO: возможность удалить
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DocsRow(
    list: List<WasteDocEntity>,
    onItemClick: (WasteDocEntity) -> Unit
) {
    // 2 colums grid
    val columnsAmount = 2
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = columnsAmount,
        modifier = Modifier.fillMaxWidth()
    ) {
        // доки
        list.forEach {
            DocItem(
                doc = it,
                modifier = Modifier.weight(1f)
            ) {
                onItemClick(it)
            }
        }

        // добивает для ровной строки
        (list.size % columnsAmount).takeIf {
            it != 0
        }?.also {
            repeat(columnsAmount - it) {
                Spacer(
                    Modifier
                        .weight(1f)
                        .height(8.dp)
                )
            }
        }
    }
}


@Composable
private fun DocItem(
    doc: WasteDocEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier.then(modifier),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (doc.files.isEmpty()) {
                Spacer(Modifier.size(56.dp))
            } else doc.files.firstOrNull()?.resource?.posterLink?.also {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                )
            }
            Spacer(Modifier.width(8.dp))
            Column {
                Text(
                    text = stringResource(getDocTypeStringId(doc.type)),
                    style = MaterialTheme.typography.labelMedium
                )
                if (doc.files.isNotEmpty()) {
                    Text(
                        text = stringResource(
                            R.string.inventory_verifications_resources_count,
                            doc.files.size.toString()
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}