package io.r3chain.features.inventory.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.features.inventory.model.DashboardViewModel
import io.r3chain.features.inventory.model.InventoryViewModel
import io.r3chain.features.inventory.ui.components.UserAvatar

@Composable
fun DashboardScreen(
    rootModel: InventoryViewModel,
    dashboardModel: DashboardViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            dashboardModel.currentUser.collectAsState(null).value?.also { user ->
                UserAvatar(
                    size = 136.dp,
                    user = user,
                    picture = dashboardModel.currentUserImage.collectAsState(null).value,
                    onClick = rootModel::navigateToProfile
                )
            }
            Text(text = "Inventory home")
        }
    }
}