package io.r3chain.core.ui.components

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.exifinterface.media.ExifInterface
import io.r3chain.core.ui.R
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun <T> BottomSelect(
    isVisible: Boolean,
    onClose: () -> Unit,
    onSelect: (T) -> Unit,
    content: @Composable ColumnScope.(optionSelect: (T) -> Unit) -> Unit
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (isVisible) ModalBottomSheet(
        onDismissRequest = onClose,
        sheetState = bottomSheetState,
        // edgeToEdge
        windowInsets = WindowInsets(0)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
                .padding(WindowInsets.navigationBarsIgnoringVisibility.asPaddingValues())
                .navigationBarsPadding()
        ) {
            content(this) { result ->
                scope.launch {
                    bottomSheetState.hide()
                }.invokeOnCompletion {
                    if (!bottomSheetState.isVisible) {
                        onSelect(result)
                        onClose()
                    }
                }
            }
        }
    }
}


@Composable
fun ImagesSelect(
    isVisible: Boolean,
    onClose: () -> Unit,
    maxSelect: Int = 1,
    onSelect: (List<Uri>) -> Unit
) {
    val context = LocalContext.current

    // запуск камеры
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) onSelect(listOf(saveBitmapToFile(context, bitmap)))
    }

    // разрешение для камеры
    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) takePictureLauncher.launch()
    }

    // выбор с диска
    val imagePickerLauncher = if (maxSelect > 1) {
        // можно выбрать несколько
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(maxSelect)
        ) { list ->
            if (list.isNotEmpty()) onSelect(list)
        }
    } else {
        // можно выбрать 1
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            if (uri != null) onSelect(listOf(uri))
        }
    }

    val onOptionSelect = remember {
        { type: ImagesSelectOption ->
            when (type) {
                // юзер выбрал сделать фото
                ImagesSelectOption.CAMERA -> when (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                )) {
                    PermissionChecker.PERMISSION_GRANTED -> takePictureLauncher.launch()
                    else -> requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
                // юзер выбрал взять из галереи
                ImagesSelectOption.GALLERY -> imagePickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }
        }
    }

    BottomSelect(
        isVisible = isVisible,
        onClose = onClose,
        onSelect = onOptionSelect
    ) { optionSelect ->
        IconActionPlate(
            title = stringResource(R.string.select_from_camera),
            icon = Icons.Outlined.PhotoCamera
        ) {
            optionSelect(ImagesSelectOption.CAMERA)
        }
        IconActionPlate(
            title = stringResource(R.string.select_from_gallery),
            icon = Icons.Outlined.Image
        ) {
            optionSelect(ImagesSelectOption.GALLERY)
        }
    }
}


private fun saveBitmapToFile(context: Context, bitmap: Bitmap): Uri {
    // NOTE: пока без try/catch, чтобы посмотреть какие могут быть ошибки
    val file = File(context.cacheDir, "camera_image_${System.currentTimeMillis()}.jpg")

    // во временный файл
    FileOutputStream(file).use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }

    // корректировка
    val correctedBitmap = correctImageOrientation(file.path, bitmap)

    // в итоговый файл
    FileOutputStream(file).use {
        correctedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, it)
    }

    return Uri.fromFile(file)
}


private fun correctImageOrientation(imagePath: String, bitmap: Bitmap): Bitmap {
    val orientation = ExifInterface(imagePath).getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    )

    val matrix = Matrix()
    when (orientation) {
//        ExifInterface.ORIENTATION_UNDEFINED,
        ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
    }

    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}

private enum class ImagesSelectOption {
    CAMERA, GALLERY
}
