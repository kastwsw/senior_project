package io.r3chain.features.inventory.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.R
import io.r3chain.data.vo.WasteCollectVO
import io.r3chain.features.inventory.model.CollectViewModel
import io.r3chain.features.inventory.model.RootViewModel
import io.r3chain.features.inventory.ui.components.GroupLabel
import io.r3chain.ui.components.ButtonStyle
import io.r3chain.ui.components.PrimaryButton
import io.r3chain.ui.components.ScreenHeader
import io.r3chain.ui.theme.R3Theme

@Composable
fun AddDispatchScreen(
    rootModel: RootViewModel,
    collectViewModel: CollectViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // header
            ScreenHeader(
                title = stringResource(R.string.inventory_add_dispatch_title),
                backAction = rootModel::navigateBack
            )
            // content
            CollectForm(
                data = collectViewModel.data,
                onAddDocument = {},
                onDone = collectViewModel::doneForm
            )
        }
    }
}

@Composable
private fun CollectForm(
    data: WasteCollectVO,
    onAddDocument: () -> Unit,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        GroupLabel(text = stringResource(R.string.inventory_label_collect_details))

        HorizontalDivider()

        GroupLabel(text = stringResource(R.string.inventory_label_collect_documents))
        PrimaryButton(
            text = stringResource(R.string.inventory_label_add_document),
            modifier = Modifier.fillMaxWidth(),
            buttonStyle = ButtonStyle.SECONDARY,
            icon = Icons.Outlined.Add,
            onClick = onAddDocument
        )

        PrimaryButton(
            text = stringResource(R.string.inventory_label_save_form),
            modifier = Modifier.fillMaxWidth(),
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
                data = WasteCollectVO(),
                onAddDocument = {},
                onDone = {}
            )
        }
    }
}
