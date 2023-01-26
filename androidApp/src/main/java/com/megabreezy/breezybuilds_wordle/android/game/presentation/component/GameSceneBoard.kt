package com.megabreezy.breezybuilds_wordle.android.game.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions

object GameSceneBoard
{
    object Tile
    {
        @Composable
        fun Component()
        {
            Box(
                modifier = Modifier
                    .width(width = LocalSceneDimensions.current.width * (61 / Scene.idealFrame().width))
                    .aspectRatio(ratio = 1f)
                    .semantics { contentDescription = TagName.TILE.toString() }
            )
        }
    }

    enum class TagName(private val id: String)
    {
        TILE(id = "game_scene_board_tile_component");

        override fun toString(): String = this.id
    }
}