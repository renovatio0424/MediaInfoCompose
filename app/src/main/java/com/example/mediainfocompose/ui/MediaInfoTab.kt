package com.example.mediainfocompose.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.ui.graphics.vector.ImageVector

enum class MediaInfoTab(
    val icon: ImageVector,
) {
    Video(
        icon = Icons.Filled.Movie,
    ),
    Image(
        icon = Icons.Filled.Image,
    ),
    Audio(
        icon = Icons.Filled.Audiotrack,
    );

    companion object {
        fun fromRoute(route: String?): MediaInfoTab =
            when (route?.substringBefore("/")) {
                Image.name -> Image
                Audio.name -> Audio
                Video.name -> Video
                null -> Video
                else -> throw IllegalArgumentException("Route $route is not recognized.")
            }
    }
}
