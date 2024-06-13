package io.r3chain.features.inventory.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.R
import io.r3chain.features.inventory.model.DetailsViewModel
import io.r3chain.features.inventory.model.RootViewModel
import io.r3chain.ui.components.ScreenHeader
import java.text.NumberFormat
import java.util.Locale

@Composable
fun WasteDetailsScreen(
    rootModel: RootViewModel,
    detailsViewModel: DetailsViewModel = hiltViewModel()
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val waste = (detailsViewModel.data ?: rootModel.intentDetails)!!

        val formatter = remember {
            NumberFormat.getInstance(Locale.getDefault()).apply {
                maximumFractionDigits = 3
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            // header
            ScreenHeader(
                title = stringResource(
                    R.string.inventory_details_weight,
                    formatter.format((waste.grams ?: 0).toDouble() / 1000)
                ),
                backAction = rootModel::navigateBack
            )
        }
    }
}