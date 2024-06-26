package io.r3chain.feature.inventory.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.r3chain.core.ui.components.AlterButton
import io.r3chain.core.ui.components.LinkButton
import io.r3chain.core.ui.components.ScreenHeader
import io.r3chain.feature.inventory.R
import io.r3chain.feature.inventory.model.DetailsViewModel
import io.r3chain.feature.inventory.model.RootViewModel
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
                    formatter.format((detailsViewModel.data.grams ?: 0).toDouble() / 1000)
                ),
                backAction = rootModel::navigateBack
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                AlterButton(
                    text = stringResource(R.string.inventory_label_edit_waste),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rootModel.navigateToWasteEdit(detailsViewModel.data)
                }
                Spacer(Modifier.height(4.dp))
                LinkButton(
                    text = stringResource(R.string.inventory_label_delete_waste),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rootModel.deleteRecord(detailsViewModel.data)
                }
            }
        }
    }
}