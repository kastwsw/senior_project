package io.r3chain.features.inventory.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    Scaffold(
        floatingActionButton = {
            FabContent()
        }
    ) {
        NavContent(it)
    }
}

@Composable
private fun NavContent(
    paddingValues: PaddingValues,
    model: InventoryViewModel = hiltViewModel()
) {
    val navigationController = rememberNavController()
    val duration = 350

    LaunchedEffect(navigationController) {
        model.navController = navigationController
    }

    NavHost(
        navController = navigationController,
        startDestination = InventoryViewModel.ScreenState.HOME.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
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

    val currentBackStackEntry = navigationController.currentBackStackEntryAsState().value
    BackHandler(
        enabled = currentBackStackEntry?.destination?.route != InventoryViewModel.ScreenState.HOME.name
    ) {
        navigationController.popBackStack()
    }
}


@Composable
private fun FabContent(
    model: InventoryViewModel = hiltViewModel()
) {
    val currentBackStackEntry = model.navController?.currentBackStackEntryAsState()
    val isVisible by remember(currentBackStackEntry) {
        derivedStateOf {
            currentBackStackEntry?.value?.destination?.route == InventoryViewModel.ScreenState.HOME.name
        }
    }
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { 2 * it }
        ),
        exit = slideOutVertically(
            targetOffsetY = { 2 * it }
        )
    ) {
        FloatingActionButton(
            shape = CircleShape,
            onClick = {
                model.navigateToProfile()
            }
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null
            )
        }
    }
}