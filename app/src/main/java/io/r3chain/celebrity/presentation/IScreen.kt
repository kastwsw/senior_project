package io.r3chain.celebrity.presentation

import androidx.compose.runtime.Composable

/**
 * Базовый интерфейс для всех объектов экранов.
 */
interface IScreen {

    @Composable
    fun Draw()
}