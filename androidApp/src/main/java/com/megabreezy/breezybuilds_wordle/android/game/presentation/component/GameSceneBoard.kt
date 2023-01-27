package com.megabreezy.breezybuilds_wordle.android.game.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.util.theme.ThemeFonts
import com.megabreezy.breezybuilds_wordle.android.util.theme.dpToSp
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard

object GameSceneBoard
{
    object Tile
    {
        val BorderStrokeKey = SemanticsPropertyKey<BorderStroke>("BorderStroke")
        val TextStyleKey = SemanticsPropertyKey<TextStyle>("TextStyle")

        private var SemanticsPropertyReceiver.gameSceneBoardTileBorderStroke by BorderStrokeKey
        private var SemanticsPropertyReceiver.gameSceneBoardTileTextStyle by TextStyleKey

        @Composable
        fun Component(options: ComponentOptions = ComponentOptions())
        {
            val borderStroke = BorderStroke(
                width = LocalSceneDimensions.current.height * (2 / Scene.idealFrame().height),
                color = MaterialTheme.colors.error
            )
            val textStyle = TextStyle(
                color = MaterialTheme.colors.onPrimary,
                fontFamily = ThemeFonts.roboto,
                fontSize = dpToSp(dp = LocalSceneDimensions.current.height * (35 / Scene.idealFrame().height)),
                fontWeight = FontWeight.Bold
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .width(width = LocalSceneDimensions.current.width * (61 / Scene.idealFrame().width))
                    .border(borderStroke)
                    .aspectRatio(ratio = 1f)
                    .semantics
                    {
                        contentDescription = TagName.TILE.toString()
                        gameSceneBoardTileBorderStroke = borderStroke
                    }
            )
            {
                if (options.letter.isNotEmpty())
                {
                    Text(
                        text = options.letter,
                        style = textStyle,
                        modifier = Modifier
                            .semantics { gameSceneBoardTileTextStyle = textStyle }
                    )
                }
            }
        }

        data class ComponentOptions(
            val state: GameBoard.Tile.State = GameBoard.Tile.State.HIDDEN,
            val letter: String = ""
        )
    }

    enum class TagName(private val id: String)
    {
        TILE(id = "game_scene_board_tile_component");

        override fun toString(): String = this.id
    }
}