package io.r3chain.feature.inventory.ui

import android.content.res.Configuration
import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.core.data.vo.WasteDocEntity
import io.r3chain.core.data.vo.WasteDocType
import io.r3chain.core.data.vo.WasteEntity
import io.r3chain.core.data.vo.WasteRecordType
import io.r3chain.core.data.vo.WasteType
import io.r3chain.core.ui.components.AlterButton
import io.r3chain.core.ui.components.LinkButton
import io.r3chain.core.ui.components.ScreenHeader
import io.r3chain.core.ui.theme.R3Theme
import io.r3chain.feature.inventory.R
import io.r3chain.feature.inventory.model.DetailsViewModel
import io.r3chain.feature.inventory.model.RootViewModel
import io.r3chain.feature.inventory.ui.components.GroupLabel
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

@Composable
fun WasteDetailsScreen(
    rootModel: RootViewModel,
    detailsViewModel: DetailsViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val formatter = remember {
            NumberFormat.getInstance(Locale.getDefault()).apply {
                maximumFractionDigits = 3
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // header
            ScreenHeader(
                title = stringResource(
                    R.string.inventory_details_weight,
                    formatter.format((detailsViewModel.data.grams ?: 0).toDouble() / 1000)
                ),
                backAction = rootModel::navigateBack
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // детальные данные записи
                WasteRecordDetails(
                    data = detailsViewModel.data,
                    onEdit = {
                        rootModel.navigateToWasteEdit(detailsViewModel.data)
                    },
                    onDelete = {
                        rootModel.deleteRecord(detailsViewModel.data)
                    }
                )
            }
        }
    }
}


@Composable
private fun WasteRecordDetails(
    data: WasteEntity,
    modifier: Modifier = Modifier,
    onEdit: () -> Unit,
    onDelete: () -> Unit
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

        val context = LocalContext.current

        // material type
        DetailsLabel(text = stringResource(R.string.details_material_type))
        DetailsValue(text = data.materialTypes.joinToString { it.name })

        // date
        data.time?.also {
            val formatter = remember(context) {
                DateFormat.getDateFormat(context)
            }
            DetailsLabel(text = stringResource(R.string.details_date))
            DetailsValue(text = formatter.format(Date(it)))
        }

        Spacer(modifier = Modifier.height(20.dp))

        GroupLabel(
            text = stringResource(R.string.inventory_label_documents),
            paddingValues = PaddingValues(bottom = 24.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // кнопки
        AlterButton(
            text = stringResource(R.string.inventory_label_edit_waste),
            modifier = Modifier.fillMaxWidth(),
            onClick = onEdit
        )
        Spacer(Modifier.height(4.dp))
        LinkButton(
            text = stringResource(R.string.inventory_label_delete_waste),
            modifier = Modifier.fillMaxWidth(),
            onClick = onDelete
        )
    }
}


@Composable
private fun DetailsLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
private fun DetailsValue(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 12.dp)
    )
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
            WasteRecordDetails(
                data = WasteEntity(
                    recordType = WasteRecordType.COLLECT,
                    geoLatLong = 0.0 to 0.0,
                    time = 0,
                    materialTypes = listOf(WasteType.HDPE, WasteType.PVC, WasteType.PP),
                    documents = listOf(
                        WasteDocEntity(type = WasteDocType.SLIP),
                        WasteDocEntity(type = WasteDocType.INVOICE),
                        WasteDocEntity(type = WasteDocType.CERT)
                    )
                ),
                onEdit = {},
                onDelete = {}
            )
        }
    }
}
