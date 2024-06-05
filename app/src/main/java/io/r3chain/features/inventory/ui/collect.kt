package io.r3chain.features.inventory.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.R
import io.r3chain.features.inventory.model.CollectViewModel
import io.r3chain.features.inventory.model.RootViewModel
import io.r3chain.ui.components.PrimaryButton
import io.r3chain.ui.components.ScreenHeader

@Composable
fun AddCollectScreen(
    rootModel: RootViewModel,
    collectViewModel: CollectViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // header
            ScreenHeader(
                title = stringResource(R.string.inventory_add_collect_title),
                backAction = rootModel::navigateBack
            )
            // content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(24.dp)
            ) {
                PrimaryButton(text = "Create record") {
                    collectViewModel.doneForm()
                }
            }
        }
    }
}
