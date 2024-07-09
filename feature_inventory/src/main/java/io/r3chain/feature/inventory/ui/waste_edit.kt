package io.r3chain.feature.inventory.ui

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.core.data.vo.FileAttachEntity
import io.r3chain.core.data.vo.WasteDocEntity
import io.r3chain.core.data.vo.WasteDocType
import io.r3chain.core.data.vo.WasteEntity
import io.r3chain.core.data.vo.WasteRecordType
import io.r3chain.core.presentation.openLink
import io.r3chain.core.ui.components.ActionPlate
import io.r3chain.core.ui.components.BottomSelect
import io.r3chain.core.ui.components.ButtonStyle
import io.r3chain.core.ui.components.DateInput
import io.r3chain.core.ui.components.PrimaryButton
import io.r3chain.core.ui.components.ScreenHeader
import io.r3chain.core.ui.components.SelectableInput
import io.r3chain.core.ui.components.TextInput
import io.r3chain.core.ui.theme.R3Theme
import io.r3chain.feature.inventory.R
import io.r3chain.feature.inventory.model.FormViewModel
import io.r3chain.feature.inventory.model.RootViewModel
import io.r3chain.feature.inventory.ui.components.DocsRow
import io.r3chain.feature.inventory.ui.components.GroupLabel
import io.r3chain.feature.inventory.ui.components.PhotoRow
import io.r3chain.feature.inventory.ui.components.RowLabel
import io.r3chain.feature.inventory.ui.components.WasteTypeSelect
import io.r3chain.feature.inventory.ui.components.WeightInput
import io.r3chain.feature.inventory.ui.components.getDocTypeStringId


@Composable
fun WasteFormScreen(
    rootModel: RootViewModel,
    formViewModel: FormViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val isNew = formViewModel.wasteData.id == 0

        Column(modifier = Modifier.fillMaxSize()) {
            val title = if (isNew) stringResource(
                when (formViewModel.wasteData.recordType) {
                    WasteRecordType.COLLECT -> R.string.inventory_add_collect_title
                    WasteRecordType.RECEIVE -> R.string.inventory_add_receive_title
                    WasteRecordType.DISPATCH -> R.string.inventory_add_dispatch_title
                }
            ) else stringResource(R.string.inventory_edit_id, formViewModel.wasteData.id)

            // header
            ScreenHeader(
                title = title,
                backAction = rootModel::navigateBack
            )
            // content
            WasteForm(
                data = formViewModel.wasteData,
                isNew = isNew,
                modifier = Modifier.weight(1f),
                enabled = !formViewModel.isLoading,
                onUriSelected = formViewModel::uploadWasteResources,
                onFileDeleted = formViewModel::deleteWasteResource,
                onDataChanged = formViewModel::changeWasteData,
                onEditDocument = {
                    formViewModel.changeVerificationData(it)
                    rootModel.navigateToWasteEditDocs()
                },
                onAddDocument = {
                    formViewModel.intentVerificationByType(it)
                    rootModel.navigateToWasteEditDocs()
                },
                onUpdate = formViewModel::updateWasteRecord,
                onCreate = formViewModel::createWasteRecord
            )
        }
    }

    // обработка результата
    LaunchedEffect(formViewModel.doneResult) {
        formViewModel.doneResult
            ?.onSuccess {
                rootModel.recordAdded(it)
            }
            ?.onFailure {
                // TODO: выдать ошибку
            }
    }
}


@Composable
private fun WasteForm(
    data: WasteEntity,
    isNew: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onUriSelected: (List<Uri>) -> Unit,
    onFileDeleted: (FileAttachEntity) -> Unit,
    onDataChanged: (WasteEntity) -> Unit,
    onEditDocument: (WasteDocEntity) -> Unit,
    onAddDocument: (WasteDocType) -> Unit,
    onUpdate: () -> Unit,
    onCreate: () -> Unit
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        GroupLabel(
            text = stringResource(
                when (data.recordType) {
                    WasteRecordType.COLLECT -> R.string.inventory_collect_details_label
                    WasteRecordType.RECEIVE -> R.string.inventory_receive_details_label
                    WasteRecordType.DISPATCH -> R.string.inventory_dispatch_details_label
                }
            )
        )

        // photos
        RowLabel(text = stringResource(R.string.inventory_label_photos))
        PhotoRow(
            data = data.files,
            onUriSelected = onUriSelected,
            onDelete = onFileDeleted
        )
        Spacer(Modifier.height(28.dp))

        // other inputs
        when (data.recordType) {
            WasteRecordType.COLLECT ->
                CollectInputs(data = data, isNew = isNew, onDataChanged = onDataChanged)
            WasteRecordType.RECEIVE ->
                ReceiveInputs(data = data, isNew = isNew, onDataChanged = onDataChanged)
            WasteRecordType.DISPATCH ->
                DispatchInputs(data = data, isNew = isNew, onDataChanged = onDataChanged)
        }

        HorizontalDivider(modifier = Modifier.padding(top = 52.dp, bottom = 46.dp))

        // documents
        VerificationDocuments(
            list = data.documents,
            onEditDocument = onEditDocument,
            onAddDocument = onAddDocument
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 46.dp))

        // done
        PrimaryButton(
            text = stringResource(R.string.inventory_label_save_form),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            onClick = {
                if (isNew) onCreate()
                else onUpdate()
            }
        )
        Spacer(Modifier.height(16.dp))
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CollectInputs(
    data: WasteEntity,
    isNew: Boolean,
    onDataChanged: (WasteEntity) -> Unit
) {
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
        RowLabel(text = stringResource(R.string.inventory_collect_date_label))
        DateInput(time = time, enabled = isNew) {
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
    val partnerOptions = remember {
        listOf("Partner 1", "Partner 2", "Partner 3", "Partner 4")
    }
    RowLabel(text = stringResource(R.string.inventory_dispatch_partner_label))
    SelectableInput(
        options = partnerOptions,
        selectedIndex = partnerOptions.indexOf(data.venue).takeIf { it >= 0 },
        placeholderValue = stringResource(R.string.inventory_dispatch_partner_hint)
    ) {
        onDataChanged(
            data.copy(venue = partnerOptions[it])
        )
    }
}


@Composable
private fun ReceiveInputs(
    data: WasteEntity,
    isNew: Boolean,
    onDataChanged: (WasteEntity) -> Unit
) {
    // date
    data.time?.also { time ->
        RowLabel(text = stringResource(R.string.inventory_receive_date_label))
        DateInput(time = time, enabled = isNew) {
            onDataChanged(
                data.copy(time = it)
            )
        }
        Spacer(Modifier.height(28.dp))
    }

    // venue
    val venueOptions = remember {
        listOf("Venue 1", "Venue 2", "Venue 3")
    }
    RowLabel(text = stringResource(R.string.inventory_receive_venue_label))
    SelectableInput(
        options = venueOptions,
        selectedIndex = venueOptions.indexOf(data.venue).takeIf { it >= 0 },
        placeholderValue = stringResource(R.string.inventory_receive_venue_hint)
    ) {
        onDataChanged(
            data.copy(venue = venueOptions[it])
        )
    }
    Spacer(Modifier.height(28.dp))

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

    // recipient
    val recipientOptions = remember {
        listOf("Recipient 1", "Recipient 2", "Recipient 3", "Recipient 4", "Recipient 5")
    }
    RowLabel(text = stringResource(R.string.inventory_receive_recipient_label))
    SelectableInput(
        options = recipientOptions,
        selectedIndex = recipientOptions.indexOf(data.recipient).takeIf { it >= 0 },
        placeholderValue = stringResource(R.string.inventory_receive_recipient_hint)
    ) {
        onDataChanged(
            data.copy(recipient = recipientOptions[it])
        )
    }
}

@Composable
private fun DispatchInputs(
    data: WasteEntity,
    isNew: Boolean,
    onDataChanged: (WasteEntity) -> Unit,
) {
    // date
    data.time?.also { time ->
        RowLabel(text = stringResource(R.string.inventory_dispatch_date_label))
        DateInput(time = time, enabled = isNew) {
            onDataChanged(
                data.copy(time = it)
            )
        }
        Spacer(Modifier.height(28.dp))
    }

    // partner
    val partnerOptions = remember {
        listOf("Partner 1", "Partner 2", "Partner 3", "Partner 4")
    }
    RowLabel(text = stringResource(R.string.inventory_dispatch_partner_label))
    SelectableInput(
        options = partnerOptions,
        selectedIndex = partnerOptions.indexOf(data.venue).takeIf { it >= 0 },
        placeholderValue = stringResource(R.string.inventory_dispatch_partner_hint)
    ) {
        onDataChanged(
            data.copy(venue = partnerOptions[it])
        )
    }
    Spacer(Modifier.height(28.dp))

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
}


@Composable
private fun VerificationDocuments(
    list: List<WasteDocEntity>,
    onEditDocument: (WasteDocEntity) -> Unit,
    onAddDocument: (WasteDocType) -> Unit
) {
    var expanded by rememberSaveable {
        mutableStateOf(false)
    }

    GroupLabel(
        text = stringResource(R.string.inventory_label_documents),
        paddingValues = PaddingValues(bottom = 24.dp)
    )

    // список документов
    if (list.isNotEmpty()) {
        DocsRow(list = list, onItemClick = onEditDocument)
        Spacer(Modifier.height(24.dp))
    }

    // добавить документ
    PrimaryButton(
        text = stringResource(R.string.inventory_label_add_document),
        modifier = Modifier.fillMaxWidth(),
        buttonStyle = ButtonStyle.SECONDARY,
        icon = Icons.Outlined.Add,
        onClick = {
            expanded = true
        }
    )

    // выбор вариантов документов
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
        WasteDocType.entries.forEach { type ->
            ActionPlate(
                title = stringResource(getDocTypeStringId(type))
            ) {
                optionSelect(type)
            }
        }
    }
}


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

@Composable
private fun Demo() {
    R3Theme {
        Surface {
            WasteForm(
                data = WasteEntity(
                    recordType = WasteRecordType.COLLECT,
//                    geoLatLong = 0.0 to 0.0,
//                    time = 0,
                    documents = listOf(
                        WasteDocEntity(type = WasteDocType.SLIP),
                        WasteDocEntity(type = WasteDocType.INVOICE),
                        WasteDocEntity(type = WasteDocType.CERT)
                    )
                ),
                isNew = false,
                onUriSelected = {},
                onFileDeleted = {},
                onDataChanged = {},
                onEditDocument = {},
                onAddDocument = {},
                onUpdate = {},
                onCreate = {}
            )
        }
    }
}
