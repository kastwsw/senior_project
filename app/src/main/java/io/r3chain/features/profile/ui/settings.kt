package io.r3chain.features.profile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import io.r3chain.features.root.ui.LocalSharedModel

@Composable
fun SettingsScreen() {
    val sharedModel = LocalSharedModel.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = sharedModel.currentUser?.firstName ?: ""
        )
    }
}