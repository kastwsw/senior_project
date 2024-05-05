package io.r3chain.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.r3chain.data.vo.UserVO

open class SharedModel : ViewModel() {

    var currentUser by mutableStateOf<UserVO?>(null)
        private set

    // TODO: делать это через апдейт БД (хз что с "не запоминать")
    fun updateUser(value: UserVO) {
        currentUser = value
    }

}