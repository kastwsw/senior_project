package io.r3chain.feature.inventory.ui

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.core.data.vo.FileAttachEntity
import io.r3chain.core.data.vo.WasteDocEntity
import io.r3chain.core.data.vo.WasteDocType
import io.r3chain.core.ui.components.AlterButton
import io.r3chain.core.ui.components.PrimaryButton
import io.r3chain.core.ui.components.ScreenHeader
import io.r3chain.core.ui.components.TextInput
import io.r3chain.core.ui.theme.R3Theme
import io.r3chain.feature.inventory.R
import io.r3chain.feature.inventory.model.FormViewModel
import io.r3chain.feature.inventory.model.RootViewModel
import io.r3chain.feature.inventory.ui.components.PhotoRow
import io.r3chain.feature.inventory.ui.components.RowLabel
import io.r3chain.feature.inventory.ui.components.getDocTypeStringId

@Composable
fun WasteDocScreen(
    rootModel: RootViewModel,
    formViewModel: FormViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // header
            ScreenHeader(
                title = stringResource(R.string.inventory_verifications_title),
                backAction = rootModel::navigateBack
            )
            formViewModel.verificationData?.also { doc ->
                WasteDocForm(
                    data = doc,
                    isNew = doc.id == 0,
                    modifier = Modifier.weight(1f),
                    onUriSelected = {
                        // загрузить файлы
                        formViewModel.uploadVerificationResources(doc = doc, uris = it)
                    },
                    onFileDeleted = {
                        formViewModel.deleteVerificationResource(it)
                    },
                    onUriSelected2 = {
                        // загрузить файлы
                        formViewModel.uploadVerificationResources2(doc = doc, uris = it)
                    },
                    onFileDeleted2 = {
                        formViewModel.deleteVerificationResource2(it)
                    },
                    onDataChanged = {
                        // проапдейтить данные
                        formViewModel.changeVerificationData(it)
                    },
                    onDelete = {
                        // удалить док из записи мусора
                        formViewModel.deleteVerification(doc)
                        rootModel.navigateBack()
                    },
                    onUpdate = {
                        // обновить док
                        formViewModel.updateVerification(doc)
                        rootModel.navigateBack()
                    },
                    onCreate = {
                        // добавить док к записи мусора
                        formViewModel.addVerification(doc)
                        rootModel.navigateBack()
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun WasteDocForm(
    data: WasteDocEntity,
    isNew: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onUriSelected: (List<Uri>) -> Unit,
    onFileDeleted: (FileAttachEntity) -> Unit,
    onUriSelected2: (List<Uri>) -> Unit,
    onFileDeleted2: (FileAttachEntity) -> Unit,
    onDataChanged: (WasteDocEntity) -> Unit,
    onDelete: () -> Unit,
    onUpdate: () -> Unit,
    onCreate: () -> Unit
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        TextField(
            value = stringResource(getDocTypeStringId(data.type)),
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = stringResource(R.string.inventory_verifications_type_group_label))
            },
            onValueChange = {}
        )
        Spacer(Modifier.height(16.dp))

        if (data.type == WasteDocType.SLIP) {
            // photo
            RowLabel(text = stringResource(R.string.inventory_label_photos_slip))
            PhotoRow(
                data = data.files,
                onUriSelected = onUriSelected,
                onDelete = onFileDeleted
            )
            Spacer(Modifier.height(16.dp))

            // number
            RowLabel(text = stringResource(R.string.inventory_vehicle_number_label))
            TextInput(
                value = data.vehicleNumber,
                modifier = Modifier.fillMaxWidth(),
                placeholderValue = stringResource(R.string.inventory_vehicle_number_hint),
                maxLength = 16,
                onValueChange = {
                    onDataChanged(
                        data.copy(vehicleNumber = it)
                    )
                }
            )
            Spacer(Modifier.height(16.dp))

            // photo
            RowLabel(text = stringResource(R.string.inventory_label_vehicle_photo))
            PhotoRow(
                data = data.files2,
                onUriSelected = onUriSelected2,
                onDelete = onFileDeleted2
            )
        } else {
            // photo
            RowLabel(text = stringResource(R.string.inventory_label_photos))
            PhotoRow(
                data = data.files,
                onUriSelected = onUriSelected,
                onDelete = onFileDeleted
            )
        }
        Spacer(Modifier.height(28.dp))

        // done
        PrimaryButton(
            text = stringResource(R.string.inventory_label_save_form),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled
        ) {
            if (isNew) onCreate()
            else onUpdate()
        }

        if (!isNew) {
            Spacer(Modifier.height(16.dp))
            AlterButton(
                text = stringResource(R.string.inventory_label_delete_waste),
                modifier = Modifier.fillMaxWidth(),
                onClick = onDelete
            )
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
            WasteDocForm(
                data = WasteDocEntity(
                    type = WasteDocType.SLIP,
                    files = listOf(
                        FileAttachEntity(uri = Uri.EMPTY, isLoading = true),
                        FileAttachEntity(uri = Uri.EMPTY, isLoading = true)
                    )
                ),
                isNew = true,
                onUriSelected = {},
                onFileDeleted = {},
                onUriSelected2 = {},
                onFileDeleted2 = {},
                onDataChanged = {},
                onDelete = {},
                onUpdate = {},
                onCreate = {}
            )
        }
    }
}
