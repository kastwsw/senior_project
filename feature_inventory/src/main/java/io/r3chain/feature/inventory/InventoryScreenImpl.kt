package io.r3chain.feature.inventory

import androidx.compose.runtime.Composable
import io.r3chain.feature.inventory.ui.InventoryScreen
import javax.inject.Inject

class InventoryScreenImpl @Inject constructor() : InventoryScreen {

    @Composable
    override fun Draw() {
        InventoryScreen()
    }
}