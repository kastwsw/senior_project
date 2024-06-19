package io.r3chain.core.data.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserExtVO(
    @PrimaryKey
    val id: Int = 0,
    val avatarLink: String = ""
)
