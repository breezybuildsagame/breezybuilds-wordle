package com.megabreezy.breezybuilds_wordle.android.game.presentation.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.util.theme.ThemeFonts
import com.megabreezy.breezybuilds_wordle.android.util.theme.dpToSp

object GameSceneHeader
{
    val TextStyleKey = SemanticsPropertyKey<TextStyle>(name = "TextStyle")

    private var SemanticsPropertyReceiver.gameSceneHeaderTextStyle by TextStyleKey

    @Composable
    fun Component(options: ComponentOptions = ComponentOptions())
    {
        val textStyle = TextStyle(
            color = MaterialTheme.colors.onPrimary,
            fontFamily = ThemeFonts.roboto,
            fontWeight = FontWeight.Black,
            fontSize = dpToSp(dp = LocalSceneDimensions.current.height * (40 / Scene.idealFrame().height)),
            lineHeight = dpToSp(dp = LocalSceneDimensions.current.height * (46.88f / Scene.idealFrame().height)),
            letterSpacing = (0.1).sp
        )

        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .aspectRatio(ratio = 390f / 100)
                .width(width = LocalSceneDimensions.current.width * (390 / Scene.idealFrame().width))
                .semantics { contentDescription = TagName.COMPONENT.toString() }
        )
        {
            Text(
                text = options.text,
                modifier = Modifier.semantics { gameSceneHeaderTextStyle = textStyle },
                style = textStyle
            )
        }
    }

    data class ComponentOptions(val text: String = "")

    enum class TagName(private val id: String)
    {
        COMPONENT(id = "game_scene_header_component");

        override fun toString(): String = this.id
    }
}