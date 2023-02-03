package com.megabreezy.breezybuilds_wordle.android.core.ui.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

open class ImageComponent
{
    @Composable
    fun getDrawableResourceIdFromImageName(name: String): Int
    {
        val context = LocalContext.current
        return remember(name) {
            context.resources.getIdentifier(
                name,
                "drawable",
                context.packageName
            )
        }
    }
}