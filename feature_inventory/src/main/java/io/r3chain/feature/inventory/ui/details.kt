package io.r3chain.feature.inventory.ui

import android.content.res.Configuration
import android.text.format.DateFormat
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import io.r3chain.core.data.vo.FileAttachEntity
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
import io.r3chain.feature.inventory.ui.components.DocsRow
import io.r3chain.feature.inventory.ui.components.GroupLabel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
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

            // tabs
            val sections = remember {
                DetailsSections.entries
            }

            val pagerState = rememberPagerState(
                initialPage = DetailsSections.DETAILS.ordinal,
                pageCount = { sections.size }
            )
            val coroutineScope = rememberCoroutineScope()

            SecondaryTabRow(
                selectedTabIndex = pagerState.currentPage
            ) {
                sections.forEach { section ->
                    Tab(
                        selected = pagerState.currentPage == section.ordinal,
                        selectedContentColor = TabRowDefaults.primaryContentColor,
                        unselectedContentColor = TabRowDefaults.secondaryContentColor,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(section.ordinal)
                            }
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 32.dp, vertical = 14.dp)
                        ) {
                            Text(
                                text = stringResource(section.labelId),
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    }
                }
            }

            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { pageIndex ->
                when (sections[pageIndex]) {
                    // детальные данные записи
                    DetailsSections.DETAILS -> WasteRecordDetails(
                        data = detailsViewModel.data,
                        onEdit = {
                            rootModel.navigateToWasteEdit(detailsViewModel.data)
                        },
                        onDelete = {
                            rootModel.deleteRecord(detailsViewModel.data)
                        }
                    )
                    // трекинг
                    DetailsSections.TRACKING -> WasteRecordTracking(
                        data = detailsViewModel.data
                    )
                }
            }

            BackHandler(enabled = pagerState.currentPage != DetailsSections.DETAILS.ordinal) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(DetailsSections.TRACKING.ordinal)
                }
            }
        }
    }
}


@Composable
private fun WasteRecordDetails(
    data: WasteEntity,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        val context = LocalContext.current

        GroupLabel(
            text = stringResource(
                when (data.recordType) {
                    WasteRecordType.COLLECT -> R.string.inventory_collect_details_label
                    WasteRecordType.RECEIVE -> R.string.inventory_receive_details_label
                    WasteRecordType.DISPATCH -> R.string.inventory_dispatch_details_label
                }
            )
        )

        // фотки
        DetailsPhotoRow(
            list = data.files
        ) {
            // TODO: открывать файл по ссылке
        }
        Spacer(modifier = Modifier.height(32.dp))

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

        Spacer(modifier = Modifier.height(32.dp))

        GroupLabel(
            text = stringResource(R.string.inventory_label_documents),
            paddingValues = PaddingValues(bottom = 24.dp)
        )
        DocsRow(list = data.documents) {
            // TODO: открывать экран с деталями документа верификации
        }
        Spacer(modifier = Modifier.height(48.dp))

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


@Composable
private fun WasteRecordTracking(
    data: WasteEntity
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Some tracking..",
            style = MaterialTheme.typography.labelLarge
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailsPhotoRow(
    list: List<FileAttachEntity>,
    onItemClick: (FileAttachEntity) -> Unit
) {
    // 4 colums grid
    val columnsAmount = 4
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        maxItemsInEachRow = columnsAmount,
        modifier = Modifier.fillMaxWidth()
    ) {
        // доки
        list.forEach {
            Image(
                painter = rememberAsyncImagePainter(it.uri),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .clickable(
                        role = Role.Button,
                        onClick = {
                            onItemClick(it)
                        }
                    )
                    .clip(shape = RoundedCornerShape(8.dp))
            )
        }

        // добивает для ровной строки
        (list.size % columnsAmount).takeIf {
            it != 0
        }?.also {
            repeat(columnsAmount - it) {
                Spacer(
                    Modifier
                        .weight(1f)
                        .height(8.dp)
                )
            }
        }
    }
}



private enum class DetailsSections(val labelId: Int) {
    DETAILS(R.string.details_tab_label_waste),
    TRACKING(R.string.details_tab_label_tracking)
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
