package io.r3chain.features.inventory.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class RootViewModel @Inject constructor() : ViewModel() {

    var navController by mutableStateOf<NavHostController?>(null)


    fun navigateBack() {
        navController?.navigateUp()
    }

    fun navigateToProfile() {
        navController?.navigate(ScreenState.PROFILE.name) {
            launchSingleTop = true
        }
    }

    fun navigateToAddCollect() {
        navController?.navigate(ScreenState.COLLECT.name) {
            launchSingleTop = true
        }
    }

    fun navigateToAddReceive() {
        navController?.navigate(ScreenState.RECEIVE.name) {
            launchSingleTop = true
        }
    }

    fun navigateToAddDispatch() {
        navController?.navigate(ScreenState.DISPATCH.name) {
            launchSingleTop = true
        }
    }

    enum class ScreenState {
        HOME, PROFILE, COLLECT, RECEIVE, DISPATCH
    }

}