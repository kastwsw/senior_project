package io.r3chain.features.root.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.r3chain.navigation.NavigationModel
import io.r3chain.ui.atoms.ErrorPlate
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
    Box(modifier = Modifier.fillMaxSize()) {
        // TODO: навигация
        if (model.currentUser == null) {
            RootScreen.Auth.Draw()
        } else {
            RootScreen.Sets.Draw()
        }

//        // есть или нет коннект
//        if (!model.hasConnection) ErrorPlate(
//            text = "!No connection",
//            modifier = Modifier.align(Alignment.BottomCenter)
//        )
    }
}

@Preview(name = "Login", showSystemUi = true)
@Composable
private fun ScreenPreview() {
    App()
}