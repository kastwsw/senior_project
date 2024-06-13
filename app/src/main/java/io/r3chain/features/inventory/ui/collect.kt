package io.r3chain.features.inventory.ui

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.R
import io.r3chain.celebrity.presentation.openLink
import io.r3chain.data.vo.WasteVO
import io.r3chain.features.inventory.model.FormViewModel
import io.r3chain.features.inventory.model.RootViewModel
import io.r3chain.features.inventory.ui.components.GroupLabel
import io.r3chain.features.inventory.ui.components.PhotosRow
import io.r3chain.features.inventory.ui.components.RowLabel
import io.r3chain.features.inventory.ui.components.VerificationDocuments
import io.r3chain.features.inventory.ui.components.WasteTypeSelect
import io.r3chain.features.inventory.ui.components.WeightInput
import io.r3chain.ui.components.DateInput
import io.r3chain.ui.components.PrimaryButton
import io.r3chain.ui.components.ScreenHeader
import io.r3chain.ui.components.SelectableInput
import io.r3chain.ui.components.TextInput
import io.r3chain.ui.theme.R3Theme

@Composable
fun AddCollectScreen(
    rootModel: RootViewModel,
    formViewModel: FormViewModel = hiltViewModel()
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
                data = formViewModel.data,
                modifier = Modifier.weight(1f),
                enabled = !formViewModel.isLoading,
                onUriSelected = formViewModel::uploadImages,
                onDataChanged = formViewModel::changeFormData,
                onAddDocument = {
                    formViewModel.currentVerificationType = it
                    rootModel.navigateToCollectDoc()
                },
                onDone = formViewModel::doneForm
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CollectForm(
    data: WasteVO,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onUriSelected: (List<Uri>) -> Unit,
    onDataChanged: (WasteVO) -> Unit,
    onAddDocument: (Int) -> Unit,
    onDone: () -> Unit
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
