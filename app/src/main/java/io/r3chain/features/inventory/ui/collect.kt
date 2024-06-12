package io.r3chain.features.inventory.ui

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import io.r3chain.R
import io.r3chain.celebrity.presentation.openLink
import io.r3chain.data.vo.FileAttachVO
import io.r3chain.data.vo.WasteVO
import io.r3chain.features.inventory.model.CollectViewModel
import io.r3chain.features.inventory.model.RootViewModel
import io.r3chain.features.inventory.ui.components.GroupLabel
import io.r3chain.features.inventory.ui.components.RowLabel
import io.r3chain.features.inventory.ui.components.WasteTypeSelect
import io.r3chain.features.inventory.ui.components.WeightInput
import io.r3chain.ui.components.ActionPlate
import io.r3chain.ui.components.BottomSelect
import io.r3chain.ui.components.ButtonStyle
import io.r3chain.ui.components.DateInput
import io.r3chain.ui.components.ImagesSelect
import io.r3chain.ui.components.PrimaryButton
import io.r3chain.ui.components.ScreenHeader
import io.r3chain.ui.components.SelectableInput
import io.r3chain.ui.components.TextInput
import io.r3chain.ui.theme.R3Theme

@Composable
fun AddCollectScreen(
    rootModel: RootViewModel,
    collectViewModel: CollectViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // header
            ScreenHeader(
                title = stringResource(R.string.inventory_add_collect_title),
                backAction = rootModel::navigateBack
            )
            // content
            CollectForm(
                data = collectViewModel.data,
                modifier = Modifier.weight(1f),
                enabled = !collectViewModel.isLoading,
                onUriSelected = collectViewModel::uploadImages,
                onDataChanged = collectViewModel::changeFormData,
                onAddDocument = {
                    collectViewModel.currentVerificationType = it
                    rootModel.navigateToAddCollectDoc()
                },
                onDone = collectViewModel::doneForm
            )
        }
    }

    // обработка результата
    LaunchedEffect(collectViewModel.doneResult) {
        collectViewModel.doneResult
            ?.onSuccess {
                rootModel.recordAdded(it)
            }
            ?.onFailure {
                // TODO: выдать ошибку
            }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CollectForm(
    data: WasteVO,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onUriSelected: (List<Uri>) -> Unit,
    onDataChanged: (WasteVO) -> Unit,
    onAddDocument: (Int) -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        GroupLabel(text = stringResource(R.string.inventory_label_collect_details))

        // photos
        RowLabel(text = stringResource(R.string.inventory_label_photos))
        PhotosRow(
            data = data.files,
            onUriSelected = onUriSelected
        )
        Spacer(Modifier.height(28.dp))

        // geo
        data.geoLatLong?.also { cords ->
            val context = LocalContext.current
            RowLabel(text = stringResource(R.string.inventory_label_geo))
            TextInput(
                value = cords.toString(),
                leadingVector = Icons.Outlined.Map,
                onClick = {
                    // открыть карту
                    context.openLink(
                        "geo:${cords.first},${cords.second}?q=${cords.first},${cords.second}"
                    )
                },
                onValueChange = {}
            )
            Spacer(Modifier.height(28.dp))
        }

        // date
        data.time?.also { time ->
            RowLabel(text = stringResource(R.string.inventory_label_date))
            DateInput(time = time) {
                onDataChanged(
                    data.copy(time = it)
                )
            }
            Spacer(Modifier.height(28.dp))
        }

        // type
        RowLabel(text = stringResource(R.string.inventory_label_materials_type))
        WasteTypeSelect(
            types = data.materialTypes,
            onSelect = {
                onDataChanged(
                    data.copy(materialTypes = it)
                )
            }
        )
        Spacer(Modifier.height(18.dp))

        // weight
        WeightInput(grams = data.grams) {
            onDataChanged(
                data.copy(grams = it)
            )
        }
        Spacer(Modifier.height(28.dp))

        // partner
        var partnerIndex by remember(data) {
            mutableStateOf<Int?>(null)
        }
        RowLabel(text = stringResource(R.string.inventory_label_partner))
        SelectableInput(
            options = listOf("Partner 1", "Partner 2", "Partner 3", "Partner 4"),
            selectedIndex = partnerIndex,
            placeholderValue = stringResource(R.string.inventory_hint_partner)
        ) {
            partnerIndex = it
        }

        HorizontalDivider(modifier = Modifier.padding(top = 52.dp, bottom = 46.dp))

        // documents
        VerificationDocuments(onAddDocument = onAddDocument)

        HorizontalDivider(modifier = Modifier.padding(vertical = 46.dp))

        // done
        PrimaryButton(
            text = stringResource(R.string.inventory_label_save_form),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            onClick = onDone
        )
        Spacer(Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PhotosRow(
    data: List<FileAttachVO>,
    onUriSelected: (List<Uri>) -> Unit
) {
    var isImageSelectVisible by rememberSaveable {
        mutableStateOf(false)
    }

    // 4 colums grid
    val columnsAmount = 4
    val shape = RoundedCornerShape(8.dp)
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = 4,
        modifier = Modifier.fillMaxWidth()
    ) {
        // фотки
        data.forEach { file ->
            if (file.isLoading) {
                // загружается
                FileBox(
                    modifier = Modifier
                        .weight(1f)
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
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(shape = shape)
                    )
                }
                // TODO: если ошибка
                // TODO: возможность удалить
            }
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
fun FileBox(
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
fun VerificationDocuments(
    onAddDocument: (Int) -> Unit
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    GroupLabel(
        text = stringResource(R.string.inventory_label_collect_documents),
        paddingValues = PaddingValues(bottom = 24.dp)
    )
    PrimaryButton(
        text = stringResource(R.string.inventory_label_add_document),
        modifier = Modifier.fillMaxWidth(),
        buttonStyle = ButtonStyle.SECONDARY,
        icon = Icons.Outlined.Add,
        onClick = {
            expanded = true
        }
    )

    val options = stringArrayResource(R.array.inventory_verifications_types)

    BottomSelect(
        isVisible = expanded,
        onClose = {
            expanded = false
        },
        onSelect = {
            expanded = false
            onAddDocument(it)
        }
    ) { optionSelect ->
        options.forEachIndexed { index, option ->
            ActionPlate(title = option) {
                optionSelect(index)
            }
        }
    }
}


@Preview(
    name = "Demo Night",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun PreviewNight() {
    Demo()
}

@Preview(
    name = "Demo Light",
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun PreviewLight() {
    Demo()
}

@Composable
private fun Demo() {
    R3Theme {
        Surface {
            CollectForm(
                data = WasteVO(
                    geoLatLong = 0.0 to 0.0,
                    time = 0
                ),
                onUriSelected = {},
                onDataChanged = {},
                onAddDocument = {},
                onDone = {}
            )
        }
    }
}
