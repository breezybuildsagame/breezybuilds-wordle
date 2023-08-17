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

object CoreTile
{
    @Composable
    fun Component(options: ComponentOptions = ComponentOptions())
    {
        val borderWidth: Float = when(options.state)
        {
            State.HIDDEN.name -> 2f / 61
            else -> 0f
        }

        val borderColor: Color = when(options.state)
        {
            State.HIDDEN.name -> MaterialTheme.colorScheme.error
            else -> Color.Transparent
        }

        val backgroundColor: Color = when(options.state)
        {
            State.CLOSE.name -> MaterialTheme.colorScheme.tertiary
            State.CORRECT.name -> MaterialTheme.colorScheme.secondary
            State.INCORRECT.name -> MaterialTheme.colorScheme.error
            else -> Color.Transparent
        }

        val borderStroke = BorderStroke(
            width = LocalSceneDimensions.current.height
                * (options.tileSize / Scene.idealFrame().height)
                * borderWidth,
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
        println("1AR: background color: $backgroundColor")

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
        val state: String = State.HIDDEN.name,
        val tileSize: Float = 61f,
        val textStyle: TextStyle? = null,
        val letter: String = ""
    )

    enum class TagName(private val id: String)
    {
        TILE(id = "core_tile_component");

        override fun toString(): String = this.id
    }

    enum class State { CLOSE, CORRECT, INCORRECT, HIDDEN; }

    val BackgroundColorKey = SemanticsPropertyKey<Color>("BackgroundColor")
    val BorderStrokeKey = SemanticsPropertyKey<BorderStroke>("BorderStroke")
    val TextStyleKey = SemanticsPropertyKey<TextStyle>("TextStyle")

    private var SemanticsPropertyReceiver.coreTileBackgroundColor by BackgroundColorKey
    private var SemanticsPropertyReceiver.coreTileBorderStroke by BorderStrokeKey
    private var SemanticsPropertyReceiver.coreTileTextStyle by TextStyleKey
}