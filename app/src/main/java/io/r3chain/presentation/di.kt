package io.r3chain.presentation

import androidx.compose.runtime.compositionLocalOf

val LocalPresenter = compositionLocalOf<Presenter> {
    error("No presenter in composition.")
}
