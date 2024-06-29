package io.r3chain.feature.inventory.ui

import android.content.res.Configuration
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
import io.r3chain.core.data.vo.WasteDocType
import io.r3chain.core.data.vo.WasteDocumentEntity
import io.r3chain.core.ui.components.PrimaryButton
import io.r3chain.core.ui.components.ScreenHeader
import io.r3chain.core.ui.theme.R3Theme
import io.r3chain.feature.inventory.R
import io.r3chain.feature.inventory.model.FormViewModel
import io.r3chain.feature.inventory.model.RootViewModel

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
            formViewModel.currentDoc?.also {
                WasteDocForm(
                    data = it,
                    isNew = true,
                    modifier = Modifier.weight(1f),
                    onDone = {
                        // добавить док к записи мусора
                        formViewModel.addDoc(it)
                        rootModel.navigateBack()
                    }
                )
            }
        }
    }
}


@Composable
private fun WasteDocForm(
    data: WasteDocumentEntity,
    isNew: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        val labelId = when (data.type) {
            WasteDocType.SLIP -> R.string.inventory_verifications_type_slip
            WasteDocType.PHOTO -> R.string.inventory_verifications_type_photo
            WasteDocType.CERT -> R.string.inventory_verifications_type_cert
            WasteDocType.INVOICE -> R.string.inventory_verifications_type_invoice
        }
        TextField(
            value = stringResource(labelId),
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = stringResource(R.string.inventory_verifications_type_group_label))
            },
            onValueChange = {}
        )

        Spacer(Modifier.height(120.dp))

        // done
        PrimaryButton(
            text = stringResource(R.string.inventory_label_save_form),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            onClick = onDone
        )
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
                data = WasteDocumentEntity(),
                isNew = true,
                onDone = {}
            )
        }
    }
}
