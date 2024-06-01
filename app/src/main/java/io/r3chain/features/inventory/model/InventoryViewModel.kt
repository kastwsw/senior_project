package io.r3chain.features.inventory.model

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.repositories.UserRepository
import javax.inject.Inject

@HiltViewModel
open class InventoryViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var navController: NavHostController? = null


    fun navigateBack() {
        navController?.popBackStack()
    }

    fun navigateToProfile() {
        navController?.navigate(ScreenState.PROFILE.name)
    }

    enum class ScreenState {
        HOME, PROFILE
    }

}