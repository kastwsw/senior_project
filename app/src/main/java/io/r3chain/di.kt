package io.r3chain

import androidx.compose.runtime.compositionLocalOf
import io.r3chain.presentation.Presenter

val LocalPresenter = compositionLocalOf<Presenter> {
    error("No presenter in composition.")
}

//val LocalNavigation = compositionLocalOf<NavHostController> {
//    error("No navigation in composition.")
//}
