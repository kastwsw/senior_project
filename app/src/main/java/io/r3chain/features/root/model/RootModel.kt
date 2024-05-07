package io.r3chain.features.root.model

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.r3chain.data.repositories.UserRepository
import io.r3chain.navigation.SharedModel
import javax.inject.Inject

@HiltViewModel
class RootModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val userRepository: UserRepository
) : SharedModel(handle, userRepository) {

}