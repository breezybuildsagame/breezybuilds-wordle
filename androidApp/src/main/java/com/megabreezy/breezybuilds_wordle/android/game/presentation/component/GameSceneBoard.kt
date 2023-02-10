package com.megabreezy.breezybuilds_wordle.android.game.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    @Composable
    fun Component(options: ComponentOptions = ComponentOptions())
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .semantics { contentDescription = TagName.BOARD.toString() },
            verticalArrangement = Arrangement.Top
        )
        {
            options.rows.forEachIndexed()
            { rowIndex, rowView ->
                rowView()

                if (
                    (options.rows.count() > 1 && rowIndex == 0)
                    || (options.rows.count() > 1 && rowIndex != 0 && rowIndex != (options.rows.count() - 1))
                )
                {
                    Spacer(
                        modifier = Modifier
                            .height(height = LocalSceneDimensions.current.height * (10 / Scene.idealFrame().height))
                            .aspectRatio(Scene.idealFrame().width / 10f)
                            .semantics { contentDescription = "spacer" }
                    )
                }
            }
        }
    }

    object Tile
    {
        val BackgroundColorKey = SemanticsPropertyKey<Color>("BackgroundColor")
        val BorderStrokeKey = SemanticsPropertyKey<BorderStroke>("BorderStroke")
        val TextStyleKey = SemanticsPropertyKey<TextStyle>("TextStyle")

        private var SemanticsPropertyReceiver.gameSceneBoardTileBackgroundColor by BackgroundColorKey
        private var SemanticsPropertyReceiver.gameSceneBoardTileBorderStroke by BorderStrokeKey
        private var SemanticsPropertyReceiver.gameSceneBoardTileTextStyle by TextStyleKey

        @Composable
        fun Component(options: ComponentOptions = ComponentOptions())
        {
            val borderWidth: Float = when(options.state)
            {
                GameBoard.Tile.State.HIDDEN -> 2f
                else -> 0f
            }

            val borderColor: Color = when(options.state)
            {
                GameBoard.Tile.State.HIDDEN -> MaterialTheme.colors.error
                else -> Color.Transparent
            }

            val backgroundColor: Color = when(options.state)
            {
                GameBoard.Tile.State.CLOSE -> MaterialTheme.colors.secondaryVariant
                GameBoard.Tile.State.CORRECT -> MaterialTheme.colors.secondary
                GameBoard.Tile.State.INCORRECT -> MaterialTheme.colors.error
                else -> Color.Transparent
            }

            val borderStroke = BorderStroke(
                width = LocalSceneDimensions.current.height * (borderWidth / Scene.idealFrame().height),
                color = borderColor
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
                    .aspectRatio(ratio = 1f)
                    .border(borderStroke)
                    .background(color = backgroundColor)
                    .semantics
                    {
                        contentDescription = TagName.TILE.toString()
                        gameSceneBoardTileBorderStroke = borderStroke
                        gameSceneBoardTileBackgroundColor = backgroundColor
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

    object Row
    {
        @Composable
        fun Component(options: ComponentOptions = ComponentOptions())
        {
            Row()
            {
                options.tiles.forEachIndexed()
                { tileIndex, tileView ->
                    tileView()

                    if (
                        (options.tiles.count() > 1 && tileIndex == 0)
                        || (options.tiles.count() > 1 && tileIndex != 0 && tileIndex != (options.tiles.count() - 1))
                    )
                    {
                        Spacer(
                            modifier = Modifier
                                .width(width = LocalSceneDimensions.current.width * (7 / Scene.idealFrame().width))
                                .aspectRatio(7f / 61)
                                .semantics { contentDescription = "spacer" }
                        )
                    }
                }
            }
        }

        data class ComponentOptions(val tiles: List<@Composable () -> Unit> = listOf())
    }

    data class ComponentOptions(val rows: List<@Composable () -> Unit> = listOf())

    enum class TagName(private val id: String)
    {
        BOARD(id = "game_scene_board_component"), TILE(id = "game_scene_board_tile_component");

        override fun toString(): String = this.id
    }
}