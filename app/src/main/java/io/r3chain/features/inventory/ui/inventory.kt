package io.r3chain.features.inventory.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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
        val duration = 350

        LaunchedEffect(Unit) {
            model.navController = navigationController
        }

        NavHost(
            navController = navigationController,
            startDestination = InventoryViewModel.ScreenState.HOME.name,
            modifier = Modifier.fillMaxSize(),
            enterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = duration)
                ) + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(durationMillis = duration)
                )
            },
            exitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = duration)
                ) + slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(durationMillis = duration)
                )
            },
            popEnterTransition = {
                fadeIn(
                    animationSpec = tween(durationMillis = duration)
                ) + slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(durationMillis = duration)
                )
            },
            popExitTransition = {
                fadeOut(
                    animationSpec = tween(durationMillis = duration)
                ) + slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(durationMillis = duration)
                )
            }
        ) {
            composable(route = InventoryViewModel.ScreenState.HOME.name) {
                DashboardScreen(rootModel = model)
            }
            composable(route = InventoryViewModel.ScreenState.PROFILE.name) {
                ProfileScreen(rootModel = model)
            }
        }

        val currentBackStackEntry by navigationController.currentBackStackEntryAsState()
        BackHandler(
            enabled = currentBackStackEntry?.destination?.route != InventoryViewModel.ScreenState.HOME.name
        ) {
            navigationController.popBackStack()
        }
    }
}