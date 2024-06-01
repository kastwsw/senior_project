package io.r3chain.features.inventory.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.r3chain.features.inventory.model.InventoryViewModel

@Composable
fun InventoryScreen(
    model: InventoryViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        val navigationController = rememberNavController()

        // navigation sets
        NavHost(
            navController = navigationController,
            startDestination = model.currentState.name,
            modifier = Modifier.fillMaxSize()
        ) {
            composable(
                route = InventoryViewModel.ScreenState.HOME.name
            ) {
                DashboardScreen(rootModel = model)
            }
            composable(
                route = InventoryViewModel.ScreenState.PROFILE.name
            ) {
                ProfileScreen(rootModel = model)
            }
        }
    }
}