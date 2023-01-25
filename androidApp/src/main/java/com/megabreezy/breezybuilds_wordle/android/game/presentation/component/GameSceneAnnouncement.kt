package com.megabreezy.breezybuilds_wordle.android.game.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.util.theme.ThemeFonts
import com.megabreezy.breezybuilds_wordle.android.util.theme.dpToSp

object GameSceneAnnouncement
{
    val SurfaceBackgroundColorKey = SemanticsPropertyKey<Color>(name = "SurfaceBackgroundColor")
    val SurfaceShapeKey = SemanticsPropertyKey<Shape>(name = "SurfaceShape")
    val TextStyleKey = SemanticsPropertyKey<TextStyle>(name = "TextStyle")
    val TextPaddingKey = SemanticsPropertyKey<Dp>(name = "TextPadding")

    private var SemanticsPropertyReceiver.gameSceneAnnouncementBgColor by SurfaceBackgroundColorKey
    private var SemanticsPropertyReceiver.gameSceneAnnouncementSurfaceShape by SurfaceShapeKey
    private var SemanticsPropertyReceiver.gameSceneAnnouncementTextStyle by TextStyleKey
    private var SemanticsPropertyReceiver.gameSceneAnnouncementTextPadding by TextPaddingKey

    @Composable
    fun Component(options: ComponentOptions = ComponentOptions())
    {
        val surfaceBg = MaterialTheme.colors.onPrimary
        val surfaceShape = RoundedCornerShape(
            corner = CornerSize(size = LocalSceneDimensions.current.height * (8 / Scene.idealFrame().height)))
        val textStyle = TextStyle(
            color = MaterialTheme.colors.primary,
            fontFamily = ThemeFonts.roboto,
            fontWeight = FontWeight.Bold,
            fontSize = dpToSp(dp = LocalSceneDimensions.current.height * (20 / Scene.idealFrame().height)),
            textAlign = TextAlign.Center
        )
        val textPadding = LocalSceneDimensions.current.height * (26 / Scene.idealFrame().height)

        Surface(
            color = MaterialTheme.colors.onPrimary,
            elevation = LocalSceneDimensions.current.height * (4 / Scene.idealFrame().height),
            shape = surfaceShape,
            modifier = Modifier
                .semantics()
                {
                    gameSceneAnnouncementBgColor = surfaceBg
                    gameSceneAnnouncementSurfaceShape = surfaceShape
                }
        )
        {
            Text(
                text = options.text,
                style = textStyle,
                modifier = Modifier
                    .padding(all = textPadding)
                    .semantics()
                    {
                        contentDescription = TagName.COMPONENT.toString()
                        gameSceneAnnouncementTextStyle = textStyle
                        gameSceneAnnouncementTextPadding = textPadding
                    }
            )
        }
    }

    data class ComponentOptions(val text: String = "")

    enum class TagName(private val id: String)
    {
        COMPONENT(id = "game_scene_announcement_component");

        override fun toString() = this.id
    }
}