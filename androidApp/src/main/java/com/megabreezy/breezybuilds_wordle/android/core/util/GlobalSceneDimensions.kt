package com.megabreezy.breezybuilds_wordle.android.core.util

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.megabreezy.breezybuilds_wordle.android.core.ui.LayoutFrame
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene

val LocalStageDimensions = compositionLocalOf { LayoutFrame(width = 0.dp, height = 0.dp) }
val LocalSceneDimensions = compositionLocalOf { LayoutFrame(width = 0.dp, height = 0.dp) }

class GlobalSceneDimensions
{
    var sceneFrame by mutableStateOf(LayoutFrame(width = 0.dp, height = 0.dp))
    var stageFrame by mutableStateOf(LayoutFrame(width = 0.dp, height = 0.dp))

    @Composable
    fun Component()
    {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        )
        {
            stageFrame = LayoutFrame(width = this.maxWidth, height = this.maxHeight)

            BoxWithConstraints(
                modifier = Modifier
                    .aspectRatio(Scene.idealFrame().width / Scene.idealFrame().height)
                    .fillMaxSize()
            )
            {
                sceneFrame = LayoutFrame(width = this.maxWidth, height = this.maxHeight)
            }
        }
    }
}

@Composable
fun rememberGlobalSceneDimensions() = remember { GlobalSceneDimensions() }