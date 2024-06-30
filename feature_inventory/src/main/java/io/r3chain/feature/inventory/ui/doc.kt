package io.r3chain.feature.inventory.ui

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
                    isNew = true,
                    modifier = Modifier.weight(1f),
                    onUriSelected = {
                        // загрузить фотки
                        formViewModel.uploadVerificationResources(doc = doc, uris = it)
                    },
                    onDelete = {
                        // удалить док из записи мусора
                        formViewModel.deleteVerification(doc)
                        rootModel.navigateBack()
                    },
                    onDone = {
                        // добавить док к записи мусора
                        formViewModel.addVerification(doc)
                        rootModel.navigateBack()
                    }
                )
            }
        }
    }
}


@Composable
private fun WasteDocForm(
    data: WasteDocEntity,
    isNew: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onUriSelected: (List<Uri>) -> Unit,
    onDelete: () -> Unit,
    onDone: () -> Unit
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
        Spacer(Modifier.height(12.dp))

        // photo
        RowLabel(text = stringResource(R.string.inventory_label_photos))
        PhotoRow(
            data = data.files,
            onUriSelected = onUriSelected
        )
        Spacer(Modifier.height(28.dp))

        // done
        PrimaryButton(
            text = stringResource(R.string.inventory_label_save_form),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            onClick = onDone
        )

        if (!isNew) {
            Spacer(Modifier.height(4.dp))
            AlterButton(
                text = stringResource(R.string.inventory_label_delete_waste),
                modifier = Modifier.fillMaxWidth(),
                onClick = onDelete
            )
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
            WasteDocForm(
                data = WasteDocEntity(
                    type = WasteDocType.CERT,
                    files = listOf(
                        FileAttachEntity(uri = Uri.EMPTY, isLoading = true),
                        FileAttachEntity(uri = Uri.EMPTY, isLoading = true)
                    )
                ),
                isNew = true,
                onUriSelected = {},
                onDelete = {},
                onDone = {}
            )
        }
    }
}
