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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.core.data.vo.FileAttachEntity
import io.r3chain.core.data.vo.WasteDocEntity
import io.r3chain.core.data.vo.WasteDocType
import io.r3chain.core.presentation.openUri
import io.r3chain.core.ui.components.AlterButton
import io.r3chain.core.ui.components.ScreenHeader
import io.r3chain.core.ui.theme.R3Theme
import io.r3chain.feature.inventory.R
import io.r3chain.feature.inventory.model.DetailsViewModel
import io.r3chain.feature.inventory.model.RootViewModel
import io.r3chain.feature.inventory.ui.components.RowLabel
import io.r3chain.feature.inventory.ui.components.getDocTypeStringId

@Composable
fun WasteDocDetailsScreen(
    rootModel: RootViewModel,
    detailsViewModel: DetailsViewModel = hiltViewModel()
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
            detailsViewModel.verificationData?.also { doc ->
                val context = LocalContext.current
                WasteDocDetails(
                    data = doc,
                    modifier = Modifier.weight(1f),
                    onFileClicked = {
                        context.openUri(it.uri)
                    },
                    onShare = {}
                )
            }
        }
    }
}


@Composable
private fun WasteDocDetails(
    data: WasteDocEntity,
    modifier: Modifier = Modifier,
    onFileClicked: (FileAttachEntity) -> Unit,
    onShare: () -> Unit
) {
    Column(
        modifier = Modifier
            .then(modifier)
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        // тип записи
        RowLabel(text = stringResource(getDocTypeStringId(data.type)))

        // фотки
        DetailsPhotoRow(
            list = data.files,
            onItemClick = onFileClicked
        )

        if (data.type == WasteDocType.SLIP) {
            Spacer(modifier = Modifier.height(32.dp))

            // number
            DetailsLabel(text = stringResource(R.string.inventory_vehicle_number_label))
            DetailsValue(text = data.vehicleNumber)
            Spacer(Modifier.height(16.dp))

            // photo
            DetailsLabel(text = stringResource(R.string.inventory_label_vehicle_photo))
            DetailsPhotoRow(
                list = data.files2,
                onItemClick = onFileClicked
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // кнопки
        AlterButton(
            text = stringResource(R.string.inventory_label_share),
            modifier = Modifier.fillMaxWidth(),
            onClick = onShare
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
            WasteDocDetails(
                data = WasteDocEntity(type = WasteDocType.SLIP),
                onFileClicked = {},
                onShare = {}
            )
        }
    }
}




