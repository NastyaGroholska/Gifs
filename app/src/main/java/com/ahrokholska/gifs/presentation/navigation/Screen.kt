package com.ahrokholska.gifs.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object HomeGraph : Screen() {
        @Serializable
        data object Home : Screen()

        @Serializable
        data class GifFull(val gifIndex: Int) : Screen()
    }
}