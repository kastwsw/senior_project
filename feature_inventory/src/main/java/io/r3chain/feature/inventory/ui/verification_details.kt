package io.r3chain.feature.inventory.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.core.ui.components.ScreenHeader
import io.r3chain.feature.inventory.R
import io.r3chain.feature.inventory.model.DetailsViewModel
import io.r3chain.feature.inventory.model.RootViewModel

@Composable
fun WasteDocDetailsScreen(
    rootModel: RootViewModel,
    detailsViewModel: DetailsViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // header
            ScreenHeader(
                title = stringResource(R.string.inventory_verifications_title),
                backAction = rootModel::navigateBack
            )
        }
    }
}
