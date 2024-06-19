package io.r3chain.features.inventory.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.R
import io.r3chain.features.inventory.model.FormViewModel
import io.r3chain.features.inventory.model.RootViewModel
import io.r3chain.core.ui.components.ScreenHeader

@Composable
fun EditWasteScreen(
    rootModel: RootViewModel,
    formViewModel: FormViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // header
            ScreenHeader(
                title = stringResource(R.string.inventory_edit_id, formViewModel.data.id),
                backAction = rootModel::navigateBack
            )
        }
    }
}
