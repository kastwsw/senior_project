package io.r3chain.features.inside.ui

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import io.r3chain.R
import io.r3chain.features.inside.model.ProfileViewModel
import io.r3chain.ui.components.PrimaryButton

@Composable
fun ProfileScreen(
    profileModel: ProfileViewModel = hiltViewModel()
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
                    text = profileModel.currentUser?.firstName ?: ""
                )
                PrimaryButton(
                    text = stringResource(R.string.sign_out_label),
                    enabled = !profileModel.isLoading
                ) {
                    profileModel.signOut()
                }
                val nav = rememberNavController()
                PrimaryButton(
                    text = "!Refresh",
                    enabled = !profileModel.isLoading
                ) {
                    nav.popBackStack()
//                    profileModel.refreshUserData()
                }
            }
        }
    }
}