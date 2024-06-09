package io.r3chain.features.inventory.ui

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.R
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
                onDataChanged = collectViewModel::changeFormData,
                onAddDocument = {},
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
    onDataChanged: (WasteVO) -> Unit,
    onAddDocument: () -> Unit,
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
        // TODO: grid max 4
        Box(
            modifier = Modifier
                .size(76.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
        )
        Spacer(Modifier.height(28.dp))

        // geo
        // TODO: показывать если есть фото
        RowLabel(text = stringResource(R.string.inventory_label_geo))
        TextInput(
            value = data.geoLatLong?.toString() ?: "",
            leadingVector = Icons.Outlined.Map,
            onClick = {
                // TODO: открыть карту
                onDataChanged(data.copy(geoLatLong = 12.09 to 48.111))
            },
            onValueChange = {}
        )
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
        Spacer(Modifier.height(28.dp))

        // date
        RowLabel(text = stringResource(R.string.inventory_label_date))
        DateInput(time = data.time) {
            onDataChanged(
                data.copy(time = it)
            )
        }
        Spacer(Modifier.height(28.dp))

        // weight
        RowLabel(text = stringResource(R.string.inventory_label_weight))
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

@Composable
fun VerificationDocuments(
    onAddDocument: () -> Unit
) {
    var expanded by remember {
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

    val context = LocalContext.current
    val options = stringArrayResource(R.array.inventory_verifications_types)

    BottomSelect(
        isVisible = expanded,
        onClose = {
            expanded = false
        },
        onSelect = {
            expanded = false
            // TODO: какая-то логика
            onAddDocument()
            Toast
                .makeText(context, options[it], Toast.LENGTH_SHORT)
                .show()
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
            CollectForm(
                data = WasteVO(),
                onDataChanged = {},
                onAddDocument = {},
                onDone = {}
            )
        }
    }
}
