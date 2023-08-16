package com.megabreezy.breezybuilds_wordle.android.core.ui.tile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

object CoreTile
{
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
            GameBoard.Tile.State.HIDDEN -> MaterialTheme.colorScheme.error
            else -> Color.Transparent
        }

        val backgroundColor: Color = when(options.state)
        {
            GameBoard.Tile.State.CLOSE -> MaterialTheme.colorScheme.tertiary
            GameBoard.Tile.State.CORRECT -> MaterialTheme.colorScheme.secondary
            GameBoard.Tile.State.INCORRECT -> MaterialTheme.colorScheme.error
            else -> Color.Transparent
        }

        val borderStroke = BorderStroke(
            width = LocalSceneDimensions.current.height * (borderWidth / Scene.idealFrame().height),
            color = borderColor
        )

        val textStyle = options.textStyle ?: TextStyle(
            color = MaterialTheme.colorScheme.onPrimary,
            fontFamily = ThemeFonts.roboto,
            fontSize = dpToSp(dp = LocalSceneDimensions.current.height
                * (options.tileSize / Scene.idealFrame().height)
                * (35f / 61f)
            ),
            fontWeight = FontWeight.Bold
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = options.modifier
                .width(width = LocalSceneDimensions.current.width * (options.tileSize / Scene.idealFrame().width))
                .aspectRatio(ratio = 1f)
                .border(borderStroke)
                .background(color = backgroundColor)
                .semantics()
                {
                    contentDescription = "${TagName.TILE}"
                    coreTileBorderStroke = borderStroke
                    coreTileBackgroundColor = backgroundColor
                    coreTileTextStyle = textStyle
                }
        )
        {
            if (options.letter.isNotEmpty())
            {
                Text(
                    text = options.letter,
                    style = textStyle
                )
            }
        }
    }

    data class ComponentOptions(
        val modifier: Modifier = Modifier,
        val state: GameBoard.Tile.State = GameBoard.Tile.State.HIDDEN,
        val tileSize: Float = 61f,
        val textStyle: TextStyle? = null,
        val letter: String = ""
    )

    enum class TagName(private val id: String)
    {
        TILE(id = "core_tile_component");

        override fun toString(): String = this.id
    }

    val BackgroundColorKey = SemanticsPropertyKey<Color>("BackgroundColor")
    val BorderStrokeKey = SemanticsPropertyKey<BorderStroke>("BorderStroke")
    val TextStyleKey = SemanticsPropertyKey<TextStyle>("TextStyle")

    private var SemanticsPropertyReceiver.coreTileBackgroundColor by BackgroundColorKey
    private var SemanticsPropertyReceiver.coreTileBorderStroke by BorderStrokeKey
    private var SemanticsPropertyReceiver.coreTileTextStyle by TextStyleKey
}