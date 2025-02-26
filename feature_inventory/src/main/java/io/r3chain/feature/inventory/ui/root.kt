package io.r3chain.feature.inventory.ui

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
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import io.r3chain.core.ui.components.BottomSelect
import io.r3chain.core.ui.components.IconActionPlate
import io.r3chain.feature.inventory.R
import io.r3chain.feature.inventory.model.DetailsViewModel
import io.r3chain.feature.inventory.model.FormViewModel
import io.r3chain.feature.inventory.model.RootViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun InventoryScreen(
    model: RootViewModel = hiltViewModel()
) {
    // снеки
    val snackHostState = remember {
        SnackbarHostState()
    }

    // видимость диалога выбора FAB-действий
    var isActionsSelectVisible by remember {
        mutableStateOf(false)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackHostState)
        },
        floatingActionButton = {
            FabContent {
                // TODO: организация или нет
                if (true) {
                    isActionsSelectVisible = true
                } else {
                    model.navigateToAddCollect()
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        NavContent(it)
    }

    WasteActionSelect(
        isVisible = isActionsSelectVisible,
        onClose = {
            isActionsSelectVisible = false
        },
        onSelect = {
            when (it) {
                WasteActionSelectOption.COLLECT -> model.navigateToAddCollect()
                WasteActionSelectOption.RECEIVE -> model.navigateToAddReceive()
                WasteActionSelectOption.DISPATCH -> model.navigateToAddDispatch()
            }
        }
    )

    val context = LocalContext.current

    // создавать снеки при появлении новых записей
    LaunchedEffect(Unit) {
        model.newRecords.collectLatest { record ->
            val result = snackHostState.showSnackbar(
                message = context.getString(R.string.inventory_record_added),
                actionLabel = context.getString(R.string.inventory_record_undo),
                duration = SnackbarDuration.Short
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    // отменить/удалить запись
                    model.undoRecordAdded(record)
                }
                SnackbarResult.Dismissed -> {}
            }
        }
    }
}

@Composable
private fun NavContent(
    paddingValues: PaddingValues,
    model: RootViewModel = hiltViewModel()
) {
    val navigationController = rememberNavController()
    val duration = 350

    LaunchedEffect(navigationController) {
        model.navController = navigationController
    }

    NavHost(
        navController = navigationController,
        startDestination = RootViewModel.ScreenState.HOME.name,
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
        // home dashboard
        composable(route = RootViewModel.ScreenState.HOME.name) {
            DashboardScreen(rootModel = model)
        }

        // user profile
        composable(route = RootViewModel.ScreenState.PROFILE.name) {
            ProfileScreen(rootModel = model)
        }

        // waste edit
        navigation(
            route = RootViewModel.ScreenState.EDIT.name,
            startDestination = RootViewModel.ScreenState.EDIT.name + RootViewModel.ScreenStateWaste.RECORD.name
        ) {
            // waste record form
            composable(
                route = RootViewModel.ScreenState.EDIT.name + RootViewModel.ScreenStateWaste.RECORD.name
            ) { backStackEntry ->
                WasteFormScreen(
                    rootModel = model,
                    formViewModel = hiltViewModel<FormViewModel, FormViewModel.ViewModelFactory>(
                        remember(backStackEntry) {
                            navigationController.getBackStackEntry(
                                route = RootViewModel.ScreenState.EDIT.name
                            )
                        }
                    ) { factory ->
                        factory.create(model.intentWaste!!)
                    }
                )
            }

            // document verification form
            composable(
                route = RootViewModel.ScreenState.EDIT.name + RootViewModel.ScreenStateWaste.DOC.name
            ) {
                WasteDocFormScreen(
                    rootModel = model,
                    formViewModel = hiltViewModel(
                        remember(it) {
                            navigationController.getBackStackEntry(
                                route = RootViewModel.ScreenState.EDIT.name
                            )
                        }
                    )
                )
            }
        }

        navigation(
            route = RootViewModel.ScreenState.DETAILS.name,
            startDestination = RootViewModel.ScreenState.DETAILS.name + RootViewModel.ScreenStateWaste.RECORD.name
        ) {
            // waste details
            composable(
                route = RootViewModel.ScreenState.DETAILS.name + RootViewModel.ScreenStateWaste.RECORD.name
            ) { backStackEntry ->
                WasteDetailsScreen(
                    rootModel = model,
                    detailsViewModel = hiltViewModel<DetailsViewModel, DetailsViewModel.ViewModelFactory>(
                        remember(backStackEntry) {
                            navigationController.getBackStackEntry(
                                route = RootViewModel.ScreenState.DETAILS.name
                            )
                        }
                    ) { factory ->
                        factory.create(model.intentWaste!!)
                    }
                )
            }

            // document verification
            composable(
                route = RootViewModel.ScreenState.DETAILS.name + RootViewModel.ScreenStateWaste.DOC.name
            ) {
                WasteDocDetailsScreen(
                    rootModel = model,
                    detailsViewModel = hiltViewModel(
                        remember(it) {
                            navigationController.getBackStackEntry(
                                route = RootViewModel.ScreenState.DETAILS.name
                            )
                        }
                    )
                )
            }
        }

    }

    // back action support
    val currentBackStackEntry = navigationController.currentBackStackEntryAsState().value
    BackHandler(
        enabled = currentBackStackEntry?.destination?.route != RootViewModel.ScreenState.HOME.name
    ) {
        navigationController.popBackStack()
    }
}


@Composable
private fun FabContent(
    model: RootViewModel = hiltViewModel(),
    onClick: () -> Unit
) {
    val currentBackStackEntry = model.navController?.currentBackStackEntryAsState()
    val isVisible by remember(currentBackStackEntry) {
        derivedStateOf {
            currentBackStackEntry?.value?.destination?.route == RootViewModel.ScreenState.HOME.name
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
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun WasteActionSelect(
    isVisible: Boolean,
    onClose: () -> Unit,
    onSelect: (WasteActionSelectOption) -> Unit
) {
    BottomSelect(
        isVisible = isVisible,
        onClose = onClose,
        onSelect = onSelect
    ) { optionSelect ->
        IconActionPlate(
            title = stringResource(R.string.inventory_add_collect_option),
            description = stringResource(R.string.inventory_add_collect_description),
            icon = Icons.Outlined.Add
        ) {
            optionSelect(WasteActionSelectOption.COLLECT)
        }
        IconActionPlate(
            title = stringResource(R.string.inventory_add_receive_option),
            icon = Icons.Outlined.Add
        ) {
            optionSelect(WasteActionSelectOption.RECEIVE)
        }
        IconActionPlate(
            title = stringResource(R.string.inventory_add_dispatch_option),
            description = stringResource(R.string.inventory_add_dispatch_description),
            icon = Icons.Outlined.LocalShipping
        ) {
            optionSelect(WasteActionSelectOption.DISPATCH)
        }
    }
}

private enum class WasteActionSelectOption {
    COLLECT, RECEIVE, DISPATCH
}
