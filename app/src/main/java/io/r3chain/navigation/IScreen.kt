package io.r3chain.navigation

import androidx.compose.runtime.Composable

/**
 * Базовый интерфейс для всех объектов экранов.
 */
interface IScreen {

    @Composable
    fun Draw()
}