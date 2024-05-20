package io.r3chain.features.inside.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.R
import io.r3chain.data.vo.UserVO
import io.r3chain.features.inside.model.ProfileViewModel
import io.r3chain.ui.components.PrimaryButton

@Composable
fun ProfileScreen(
    profileModel: ProfileViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // header
            Header(
                backAction = profileModel::signOut
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // user
                UserPanel(profileModel.currentUser ?: UserVO())

                // settings
            }

            // buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // contact
                PrimaryButton(
                    text = stringResource(R.string.contact_us_label),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // TODO: open mailto:
                }

                // sign out
                PrimaryButton(
                    text = stringResource(R.string.sign_out_label),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !profileModel.isLoading,
                    onClick = profileModel::signOut
                )
            }
        }
    }
}


@Composable
private fun Header(backAction: (() -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
    ) {
        IconButton(onClick = {
            backAction?.invoke()
        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = stringResource(R.string.profile_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(top = 10.dp)
                .padding(horizontal = 6.dp)
        )
    }
}

@Composable
private fun UserPanel(user: UserVO) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // logo
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            user.firstName.getOrNull(0)?.toString()?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Spacer(Modifier.height(16.dp))

        // name
        Text(
            text = user.firstName,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(4.dp))

        // email
        Text(
            text = user.email,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium
        )
    }
}