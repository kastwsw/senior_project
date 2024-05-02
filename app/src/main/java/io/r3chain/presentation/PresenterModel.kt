package io.r3chain.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.r3chain.presentation.vo.UserVO

class PresenterModel : ViewModel() {

    var currentUser by mutableStateOf<UserVO?>(null)

}