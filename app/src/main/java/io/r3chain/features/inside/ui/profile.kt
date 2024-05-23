package io.r3chain.features.inside.ui

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.R
import io.r3chain.data.vo.UserVO
import io.r3chain.features.inside.model.ProfileViewModel
import io.r3chain.ui.components.ActionPlate
import io.r3chain.ui.components.BottomSelect
import io.r3chain.ui.components.ButtonStyle
import io.r3chain.ui.components.IconActionPlate
import io.r3chain.ui.components.LinkButton
import io.r3chain.ui.components.PrimaryButton
import io.r3chain.ui.components.SwitchPlate
import io.r3chain.ui.theme.R3Theme
import java.io.File
import java.io.FileOutputStream

@Composable
fun ProfileScreen(
    profileModel: ProfileViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        profileModel.refreshUserData()
    }

    var isImageSelectVisible by rememberSaveable {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val context = LocalContext.current
        Column(modifier = Modifier.fillMaxSize()) {
            // header
            Header(
                backAction = profileModel::signOut
            )

            (profileModel.currentUser ?: UserVO()).also { user ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    // user
                    UserPanel(user) {
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
                    ActionPlate(text = stringResource(R.string.help_label)) {
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

    ImagesSelect(
        isVisible = isImageSelectVisible,
        onClose = {
            isImageSelectVisible = false
        },
        onSelect = {
            println(it)
        }
    )
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
private fun UserPanel(
    user: UserVO,
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
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .clickable(
                    indication = rememberRipple(bounded = true, radius = 50.dp),
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = onEditImage
                ),
            contentAlignment = Alignment.Center
        ) {
            user.firstName.firstOrNull()?.toString()?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = 4.dp)
                    .size(24.dp)
                    .background(
                        color = MaterialTheme.colorScheme.inverseSurface,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.inverseOnSurface
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


@Composable
fun ImagesSelect(
    isVisible: Boolean,
    maxAmount: Int = 1,
    onClose: () -> Unit,
    onSelect: (Uri) -> Unit
) {
    val context = LocalContext.current

    // запуск камеры
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) onSelect(saveBitmapToFile(context, bitmap))
    }

    // разрешение для камеры
    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) takePictureLauncher.launch()
    }

    val onOptionSelect = remember {{ type: ImagesSelectOption ->
        when (type) {
            // юзер выбрал сделать фото
            ImagesSelectOption.CAMERA -> when (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)) {
                PermissionChecker.PERMISSION_GRANTED -> takePictureLauncher.launch()
                else -> requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            // юзер выбрал взять из галереи
            ImagesSelectOption.GALLERY -> {
                println("Gallery select")
            }
        }
    }}

    BottomSelect(
        isVisible = isVisible,
        onClose = onClose,
        onSelect = onOptionSelect
    ) { optionSelect ->
        IconActionPlate(
            text = stringResource(R.string.select_from_camera),
            icon = Icons.Outlined.PhotoCamera
        ) {
            optionSelect(ImagesSelectOption.CAMERA)
        }
        IconActionPlate(
            text = stringResource(R.string.select_from_gallery),
            icon = Icons.Outlined.Image
        ) {
            optionSelect(ImagesSelectOption.GALLERY)
        }
    }
}

private enum class ImagesSelectOption {
    CAMERA, GALLERY
}

private fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
    // NOTE: пока без try/catch, чтобы посмотреть какие могут быть ошибки
    val file = File(context.cacheDir, "camera_image_${System.currentTimeMillis()}.jpg")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
    outputStream.flush()
    outputStream.close()
    return Uri.fromFile(file)
}

@Preview(
    name = "User"
)
@Composable
fun UserPanelPreview() {
    R3Theme {
        UserPanel(
            user = UserVO(
                firstName = "User Name",
                email = "john@doe.com"
            )
        ) {}
    }
}