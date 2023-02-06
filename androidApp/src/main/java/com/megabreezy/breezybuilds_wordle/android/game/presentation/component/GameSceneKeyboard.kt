package com.megabreezy.breezybuilds_wordle.android.game.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.ui.image.ImageComponent
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard

object GameSceneKeyboard
{
    object Key:ImageComponent()
    {
        var ButtonShapeKey = SemanticsPropertyKey<RoundedCornerShape>("ButtonShape")
        var ButtonColorsKey = SemanticsPropertyKey<ButtonColors>("ButtonColors")

        private var SemanticsPropertyReceiver.gameSceneKeyboardKeyButtonShape by ButtonShapeKey
        private var SemanticsPropertyReceiver.gameSceneKeyboardKeyButtonColors by ButtonColorsKey

        @Composable
        fun Component(options: ComponentOptions = ComponentOptions())
        {
            val buttonColors = ButtonDefaults.buttonColors(
                containerColor = when (options.backgroundColor)
                {
                    GameKeyboard.Key.BackgroundColor.DEFAULT -> MaterialTheme.colorScheme.background
                    GameKeyboard.Key.BackgroundColor.NOT_FOUND -> MaterialTheme.colorScheme.error
                    GameKeyboard.Key.BackgroundColor.NEARBY -> MaterialTheme.colorScheme.tertiary
                    else -> Color.Transparent
                }
            )
            val buttonShape = RoundedCornerShape(
                corner = CornerSize(size = LocalSceneDimensions.current.height * (4 / Scene.idealFrame().height))
            )
            val buttonWidth = if ((options.letters?.count() ?: 0) > 1 || options.resourceId != null) 52 else 33

            Button(
                colors = buttonColors,
                contentPadding = PaddingValues(all = 0.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .width(width = LocalSceneDimensions.current.width * (buttonWidth / Scene.idealFrame().width))
                    .aspectRatio(ratio = buttonWidth / 56f)
                    .semantics()
                    {
                        contentDescription = TagName.KEY.toString()
                        gameSceneKeyboardKeyButtonShape = buttonShape
                        gameSceneKeyboardKeyButtonColors = buttonColors
                    },
                shape = buttonShape,
                onClick = {  }
            )
            {
                options.letters?.let()
                {
                    Text(
                        text = options.letters
                    )
                }

                options.resourceId?.let()
                {
                    Image(
                        painter = painterResource(id = getDrawableResourceIdFromImageName(name = it)),
                        contentDescription = "Key Icon",
                        modifier = Modifier
                            .height(height = LocalSceneDimensions.current.height * (23 / Scene.idealFrame().height))
                            .aspectRatio(ratio = 1f)
                    )
                }
            }
        }

        data class ComponentOptions(
            val letters: String? = null,
            val resourceId: String? = null,
            val backgroundColor: GameKeyboard.Key.BackgroundColor = GameKeyboard.Key.BackgroundColor.DEFAULT
        )
    }

    enum class TagName(private val id: String)
    {
        KEY(id = "game_scene_keyboard_key_component");

        override fun toString() = this.id
    }
}