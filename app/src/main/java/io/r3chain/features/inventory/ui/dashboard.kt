package io.r3chain.features.inventory.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.valentinilk.shimmer.shimmer
import io.r3chain.R
import io.r3chain.data.vo.UserVO
import io.r3chain.data.vo.WasteVO
import io.r3chain.features.inventory.model.DashboardViewModel
import io.r3chain.features.inventory.model.RootViewModel
import io.r3chain.features.inventory.ui.components.UserAvatar
import io.r3chain.ui.theme.R3Theme
import kotlinx.coroutines.launch

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
                Sections.entries
            }

            val pagerState = rememberPagerState(
                initialPage = Sections.INVENTORY.ordinal,
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
                    Sections.INVENTORY -> InventoryContent(
                        data = dashboardModel.inventoryList.collectAsState(
                            emptyList()
                        ).value,
                        onItemClick = rootModel::navigateToWasteDetails
                    )

                    Sections.DISPATCHED -> DispatchedContent(
                        data = dashboardModel.dispatchedList.collectAsState(
                            emptyList()
                        ).value,
                        onItemClick = rootModel::navigateToWasteDetails
                    )
                }
            }

            BackHandler(enabled = pagerState.currentPage != Sections.INVENTORY.ordinal) {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(Sections.INVENTORY.ordinal)
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
    data: List<WasteVO>,
    onItemClick: (WasteVO) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp)
    ) {
        if (data.isEmpty()) EmptyListText(
            text = stringResource(R.string.inventory_tab_empty_inventory),
            modifier = Modifier.align(Alignment.Center)
        ) else RecordsList(
            data = data,
            onClick = onItemClick
        )
    }
}


@Composable
private fun DispatchedContent(
    data: List<WasteVO>,
    onItemClick: (WasteVO) -> Unit
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
            onClick = onItemClick
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


@Composable
private fun RecordsList(
    data: List<WasteVO>,
    onClick: (WasteVO) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Filters",
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(bottom = 32.dp),
        ) {
            items(
                items = data,
                key = { it.id }
            ) { item ->
                WasteItem(
                    data = item,
                    onClick = onClick
                )
            }
        }
    }
}


@Composable
private fun WasteItem(
    data: WasteVO,
    onClick: (WasteVO) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    onClick(data)
                },
                onClickLabel = data.id.toString(),
                role = Role.Button
            )
    ) {
        Text(
            text = data.id.toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        HorizontalDivider()
    }
}


private enum class Sections(val labelId: Int) {
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
            HeadCard(
                totalAmount = "100",
                user = UserVO(
                    firstName = "User Name",
                    email = "john@doe.com"
                )
            ) {}
        }
    }
}