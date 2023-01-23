package com.megabreezy.breezybuilds_wordle.android.game.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions

object GameSceneHeader
{
    @Composable
    fun Component()
    {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .aspectRatio(ratio = 390f / 100)
                .width(width = LocalSceneDimensions.current.width * (390 / Scene.idealFrame().width))
                .background(Color.Red)
                .semantics { contentDescription = TagName.COMPONENT.toString() }
        )
        {

        }
    }

    enum class TagName(private val id: String)
    {
        COMPONENT(id = "game_scene_header_component");

        override fun toString(): String = this.id
    }
}