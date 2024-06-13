package io.r3chain.features.inventory.ui

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.R
import io.r3chain.data.vo.WasteVO
import io.r3chain.features.inventory.model.FormViewModel
import io.r3chain.features.inventory.model.RootViewModel
import io.r3chain.features.inventory.ui.components.GroupLabel
import io.r3chain.features.inventory.ui.components.PhotosRow
import io.r3chain.features.inventory.ui.components.RowLabel
import io.r3chain.features.inventory.ui.components.VerificationDocuments
import io.r3chain.features.inventory.ui.components.WasteTypeSelect
import io.r3chain.features.inventory.ui.components.WeightInput
import io.r3chain.ui.components.PrimaryButton
import io.r3chain.ui.components.ScreenHeader
import io.r3chain.ui.theme.R3Theme

@Composable
fun AddReceiveScreen(
    rootModel: RootViewModel,
    formViewModel: FormViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // header
            ScreenHeader(
                title = stringResource(R.string.inventory_add_receive_title),
                backAction = rootModel::navigateBack
            )
            // content
            ReceiveForm(
                data = formViewModel.data,
                modifier = Modifier.weight(1f),
                enabled = !formViewModel.isLoading,
                onUriSelected = formViewModel::uploadImages,
                onDataChanged = formViewModel::changeFormData,
                onAddDocument = {
                    formViewModel.currentVerificationType = it
                    rootModel.navigateToReceiveDoc()
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

@Composable
private fun ReceiveForm(
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
        GroupLabel(text = stringResource(R.string.inventory_label_receive_details))

        // photos
        RowLabel(text = stringResource(R.string.inventory_label_photos))
        PhotosRow(
            data = data.files,
            onUriSelected = onUriSelected
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
        Spacer(Modifier.height(18.dp))

        // weight
        WeightInput(grams = data.grams) {
            onDataChanged(
                data.copy(grams = it)
            )
        }
        Spacer(Modifier.height(28.dp))

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
            ReceiveForm(
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
