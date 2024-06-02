package io.r3chain.features.inventory.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.R
import io.r3chain.data.vo.ResourceVO
import io.r3chain.data.vo.UserVO
import io.r3chain.features.inventory.model.ProfileViewModel
import io.r3chain.features.inventory.model.RootViewModel
import io.r3chain.features.inventory.ui.components.UserAvatar
import io.r3chain.ui.components.ActionPlate
import io.r3chain.ui.components.ButtonStyle
import io.r3chain.ui.components.ImageSelect
import io.r3chain.ui.components.LinkButton
import io.r3chain.ui.components.PrimaryButton
import io.r3chain.ui.components.ScreenHeader
import io.r3chain.ui.components.SwitchPlate
import io.r3chain.ui.theme.R3Theme

@Composable
fun ProfileScreen(
    rootModel: RootViewModel,
    profileModel: ProfileViewModel = hiltViewModel()
) {
    var isImageSelectVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val context = LocalContext.current
        Column(modifier = Modifier.fillMaxSize()) {
            // header
            ScreenHeader(
                title = stringResource(R.string.profile_title),
                backAction = rootModel::navigateBack
            )

            (profileModel.currentUser.collectAsState(null).value ?: UserVO()).also { user ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    // user
                    UserPanel(
                        user = user,
                        picture = profileModel.currentUserImage.collectAsState(null).value
                    ) {
                        isImageSelectVisible = true
                    }

                    // notifications
                    SwitchPlate(
                        text = stringResource(R.string.notification_label),
                        checked = user.sendEmailNotifications,
                        enabled = !profileModel.isLoading
                    ) {
                        profileModel.setEmailNotification(it)
                    }

                    // help
                    ActionPlate(title = stringResource(R.string.help_label)) {
                        profileModel.openHelp(context)
                    }
                }
            }

            // buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // contact
                PrimaryButton(
                    text = stringResource(R.string.support_label),
                    buttonStyle = ButtonStyle.SECONDARY,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    profileModel.openSupport(context)
                }
                // sign out
                LinkButton(
                    text = stringResource(R.string.sign_out_label),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !profileModel.isLoading,
                    onClick = profileModel::signOut
                )
            }
        }
    }

    ImageSelect(
        isVisible = isImageSelectVisible,
        onClose = {
            isImageSelectVisible = false
        },
        onSelect = {
            profileModel.uploadImage(it)
        }
    )
}


@Composable
private fun UserPanel(
    user: UserVO,
    picture: ResourceVO? = null,
    onEditImage: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // logo
        UserAvatar(
            size = 100.dp,
            user = user,
            picture = picture,
            hasEditSymbol = true,
            onClick = onEditImage
        )
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

@Preview(
    name = "User"
)
@Composable
private fun UserPanelPreview() {
    R3Theme {
        Surface {
            UserPanel(
                user = UserVO(
                    firstName = "User Name",
                    email = "john@doe.com"
                )
            ) {}
        }
    }
}