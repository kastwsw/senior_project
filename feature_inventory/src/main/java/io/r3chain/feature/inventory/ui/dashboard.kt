package io.r3chain.feature.inventory.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.valentinilk.shimmer.shimmer
import io.r3chain.core.data.vo.UserVO
import io.r3chain.core.data.vo.WasteEntity
import io.r3chain.core.ui.theme.R3Theme
import io.r3chain.feature.inventory.R
import io.r3chain.feature.inventory.model.DashboardViewModel
import io.r3chain.feature.inventory.model.RootViewModel
import io.r3chain.feature.inventory.ui.components.UserAvatar
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    rootModel: RootViewModel,
    dashboardModel: DashboardViewModel = hiltViewModel()
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // card
            dashboardModel.currentUser.collectAsState(null).value?.also { user ->
                HeadCard(
                    totalAmount = dashboardModel.totalWeight.toString(),
                    user = user,
                    isLoading = dashboardModel.isLoading,
                    picture = dashboardModel.currentUserExt.collectAsState(null).value?.avatarLink,
                    onAvatarClick = rootModel::navigateToProfile
                )
            }
            Spacer(Modifier.height(8.dp))

            // tabs
            val sections = remember {
                DashboardSections.entries
            }

            val pagerState = rememberPagerState(
                initialPage = DashboardSections.INVENTORY.ordinal,
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
                    DashboardSections.INVENTORY -> InventoryContent(
                        data = dashboardModel.inventoryList.collectAsState(
                            emptyList()
                        ).value,
                        onEdit = rootModel::navigateToWasteEdit,
                        onDelete = rootModel::deleteRecord,
                        onDetails = rootModel::navigateToWasteDetails
                    )

                    DashboardSections.DISPATCHED -> DispatchedContent(
                        data = dashboardModel.dispatchedList.collectAsState(
                            emptyList()
                        ).value,
                        onEdit = rootModel::navigateToWasteEdit,
                        onDelete = rootModel::deleteRecord,
                        onDetails = rootModel::navigateToWasteDetails
                    )
                }
            }

            BackHandler(enabled = pagerState.currentPage != DashboardSections.INVENTORY.ordinal) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(DashboardSections.INVENTORY.ordinal)
                }
            }
        }
    }
}


@Composable
private fun HeadCard(
    totalAmount: String,
    user: UserVO,
    isLoading: Boolean = false,
    picture: String? = null,
    onAvatarClick: () -> Unit
) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // content
            Row(
                modifier = Modifier
                    .padding(vertical = 32.dp)
                    .padding(start = 12.dp, end = 76.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.home),
                    contentDescription = null,
                    modifier = Modifier
                        .width(83.dp)
                        .offset(y = 2.dp)
                )
                Spacer(Modifier.width(20.dp))
                Column {
                    Text(
                        text = stringResource(R.string.inventory_total_title),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .let {
                                if (!isLoading) it else it
                                    .shimmer()
                                    .background(
                                        color = MaterialTheme.colorScheme.outlineVariant,
                                        shape = MaterialTheme.shapes.small
                                    )
                            }
                    ) {
                        Text(
                            text = if (isLoading) "" else totalAmount,
                            modifier = Modifier.defaultMinSize(minWidth = 90.dp),
                            style = MaterialTheme.typography.headlineLarge
                        )
                    }
                }
            }
            // avatar
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.TopEnd)
            ) {
                UserAvatar(
                    user = user,
                    picture = picture,
                    size = 40.dp,
                    letterStyle = MaterialTheme.typography.titleLarge,
                    onClick = onAvatarClick
                )
            }
        }
    }
}


@Composable
private fun InventoryContent(
    data: List<WasteEntity>,
    onEdit: (WasteEntity) -> Unit,
    onDelete: (WasteEntity) -> Unit,
    onDetails: (WasteEntity) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        if (data.isEmpty()) EmptyListText(
            text = stringResource(R.string.inventory_tab_empty_inventory),
            modifier = Modifier.align(Alignment.Center)
        ) else RecordsList(
            data = data,
            onEdit = onEdit,
            onDelete = onDelete,
            onDetails = onDetails
        )
    }
}


@Composable
private fun DispatchedContent(
    data: List<WasteEntity>,
    onEdit: (WasteEntity) -> Unit,
    onDelete: (WasteEntity) -> Unit,
    onDetails: (WasteEntity) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp)
    ) {
        if (data.isEmpty()) EmptyListText(
            text = stringResource(R.string.inventory_tab_empty_dispatched),
            modifier = Modifier.align(Alignment.Center)
        ) else RecordsList(
            data = data,
            onEdit = onEdit,
            onDelete = onDelete,
            onDetails = onDetails
        )
    }
}


@Composable
private fun EmptyListText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .then(modifier)
            .padding(56.dp)
    )
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RecordsList(
    data: List<WasteEntity>,
    onEdit: (WasteEntity) -> Unit,
    onDelete: (WasteEntity) -> Unit,
    onDetails: (WasteEntity) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // filters
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // date
            InputChip(
                selected = false,
                label = {
                    Text(text = stringResource(R.string.filter_label_date))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.CalendarToday,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                onClick = {}
            )

            // type
            FilterChip(
                selected = false,
                label = {
                    Text(text = stringResource(R.string.filter_label_type))
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                onClick = {}
            )

            // partner
            FilterChip(
                selected = false,
                label = {
                    Text(text = stringResource(R.string.filter_label_partner))
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                onClick = {}
            )
        }

        val formatter = remember {
            NumberFormat.getInstance(Locale.getDefault()).apply {
                maximumFractionDigits = 3
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(
                start = 16.dp, top = 16.dp, end = 16.dp, bottom = 32.dp
            ),
        ) {
            items(
                items = data,
                key = { it.id }
            ) { item ->
                WasteCard(
                    data = item,
                    formatter = formatter,
                    modifier = Modifier.animateItemPlacement(),
                    onEdit = onEdit,
                    onDelete = onDelete,
                    onDetails = onDetails
                )
            }
        }
    }
}


@Composable
private fun WasteCard(
    data: WasteEntity,
    formatter: NumberFormat,
    modifier: Modifier = Modifier,
    onEdit: (WasteEntity) -> Unit,
    onDelete: (WasteEntity) -> Unit,
    onDetails: (WasteEntity) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = {
            onDetails(data)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                // фото
                data.files.firstOrNull()?.resource?.posterLink?.also {
                    Image(
                        painter = rememberAsyncImagePainter(it),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(shape = RoundedCornerShape(8.dp))
                    )
                }
                // данные
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    data.grams?.also {
                        Text(
                            text = stringResource(
                                R.string.inventory_details_weight,
                                formatter.format(it.toDouble() / 1000)
                            ),
                            style = MaterialTheme.typography.titleSmall
                        )
                        Spacer(Modifier.height(4.dp))
                    }
                    Text(
                        text = data.materialTypes.joinToString(limit = 3) { it.name },
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = data.partner.takeIf { it.isNotBlank() }
                            ?: data.venue.takeIf { it.isNotBlank() }
                            ?: data.recipient.takeIf { it.isNotBlank() }
                            ?: "",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }

            Box(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                // menu
                var expanded by remember {
                    mutableStateOf(false)
                }

                IconButton(
                    onClick = {
                        expanded = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.MoreVert,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    WasteMenuItem(
                        label = stringResource(R.string.inventory_label_edit_waste),
                        icon = Icons.Outlined.Edit
                    ) {
                        expanded = false
                        onEdit(data)
                    }
                    WasteMenuItem(
                        label = stringResource(R.string.inventory_label_delete_waste),
                        icon = Icons.Outlined.Delete
                    ) {
                        expanded = false
                        onDelete(data)
                    }
                }
            }
        }
    }
}


@Composable
private fun WasteMenuItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    DropdownMenuItem(
        text = {
            Text(text = label)
        },
        onClick = onClick,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    )
}


private enum class DashboardSections(val labelId: Int) {
    INVENTORY(R.string.inventory_tab_label_inventory),
    DISPATCHED(R.string.inventory_tab_label_dispatched)
}


@Preview(
    name = "Card"
)
@Composable
private fun HeadCardPreview() {
    R3Theme {
        Surface {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                HeadCard(
                    totalAmount = "100",
                    user = UserVO(
                        firstName = "User Name",
                        email = "john@doe.com"
                    )
                ) {}

                val formatter = remember {
                    NumberFormat.getInstance(Locale.getDefault()).apply {
                        maximumFractionDigits = 3
                    }
                }
                WasteCard(
                    data = dummyWasteRecord,
                    formatter = formatter,
                    onEdit = {},
                    onDelete = {},
                    onDetails = {}
                )
            }
        }
    }
}