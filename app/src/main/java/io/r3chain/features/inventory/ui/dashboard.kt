package io.r3chain.features.inventory.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.features.inventory.model.DashboardViewModel
import io.r3chain.features.inventory.model.InventoryViewModel
import io.r3chain.ui.components.PrimaryButton

@Composable
fun DashboardScreen(
    rootModel: InventoryViewModel,
    dashboardModel: DashboardViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            PrimaryButton(text = "To profile") {
                rootModel.navigateToProfile()
            }
            Text(text = "Inventory home")
        }
    }
}