package io.r3chain.features.auth.presentation

import androidx.compose.runtime.compositionLocalOf

val LocalModel = compositionLocalOf<Model> {
    error("No instance in composition.")
}
