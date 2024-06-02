package io.r3chain.features.inventory.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class InventoryViewModel @Inject constructor() : ViewModel() {

    var navController by mutableStateOf<NavHostController?>(null)


    fun navigateBack() {
        navController?.popBackStack()
    }

    fun navigateToProfile() {
        navController?.navigate(ScreenState.PROFILE.name) {
            launchSingleTop = true
        }
    }

    enum class ScreenState {
        HOME, PROFILE
    }

}