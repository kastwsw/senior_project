package io.r3chain.features.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.r3chain.R
import io.r3chain.features.profile.model.ProfileModel
import io.r3chain.navigation.NavigationModel
import io.r3chain.ui.atoms.PrimaryButton

@Composable
fun SettingsScreen(
    navigationModel: NavigationModel = viewModel(),
    profileModel: ProfileModel = viewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = navigationModel.currentUser?.firstName ?: ""
                )
                PrimaryButton(
                    text = stringResource(R.string.action_sign_out),
                    enabled = !profileModel.isLoading
                ) {
                    profileModel.signOut()
                }
            }
        }
    }
}