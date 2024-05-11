package io.r3chain.features.root.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import io.r3chain.navigation.NavigationModel
import io.r3chain.ui.theme.R3Theme

@Composable
fun App() {
    R3Theme {
        Content()
    }
}

@Composable
private fun Content(
    model: NavigationModel = viewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
    ) {
        // TODO: навигация
        if (model.currentUser == null) {
            RootScreen.Auth.Draw()
        } else {
            RootScreen.Sets.Draw()
        }

//        // есть или нет коннект
//        if (!model.hasConnection) ErrorPlate(
//            text = stringResource(R.string.error_no_connection),
//            modifier = Modifier.align(Alignment.BottomCenter)
//        )
    }
}
