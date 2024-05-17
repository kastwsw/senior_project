package io.r3chain.presentation

import androidx.compose.runtime.Composable

/**
 * Базовый интерфейс для всех объектов экранов.
 */
interface IScreen {

    @Composable
    fun Draw()
}