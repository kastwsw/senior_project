package io.r3chain.features.inventory.ui

import android.content.res.Configuration
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
import androidx.compose.material.icons.outlined.Add
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
import io.r3chain.features.inventory.model.CollectViewModel
import io.r3chain.features.inventory.model.RootViewModel
import io.r3chain.features.inventory.ui.components.GroupLabel
import io.r3chain.features.inventory.ui.components.RowLabel
import io.r3chain.features.inventory.ui.components.WasteTypeSelect
import io.r3chain.ui.components.ButtonStyle
import io.r3chain.ui.components.DateInput
import io.r3chain.ui.components.PrimaryButton
import io.r3chain.ui.components.ScreenHeader
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
                // TODO: выдать ошибку?
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

        RowLabel(text = stringResource(R.string.inventory_label_materials_type))
        WasteTypeSelect(
            types = data.materialTypes,
            onSelect = {
                onDataChanged(
                    data.copy(materialTypes = it)
                )
            }
        )

        DateInput(
            time = data.time,
            onTimeChange = {
                onDataChanged(
                    data.copy(time = it)
                )
            }
        )

        Spacer(Modifier.height(32.dp))
        HorizontalDivider()

        GroupLabel(text = stringResource(R.string.inventory_label_collect_documents))
        PrimaryButton(
            text = stringResource(R.string.inventory_label_add_document),
            modifier = Modifier.fillMaxWidth(),
            buttonStyle = ButtonStyle.SECONDARY,
            icon = Icons.Outlined.Add,
            onClick = onAddDocument
        )

        Spacer(Modifier.height(32.dp))

        PrimaryButton(
            text = stringResource(R.string.inventory_label_save_form),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            onClick = onDone
        )
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
