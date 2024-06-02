package io.r3chain.features.inventory.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import io.r3chain.R
import io.r3chain.features.inventory.model.RootViewModel
import io.r3chain.ui.components.ScreenHeader

@Composable
fun AddCollectScreen(rootModel: RootViewModel) {
    ScreenHeader(
        title = stringResource(R.string.inventory_add_collect_title),
        backAction = rootModel::navigateBack
    )
}
