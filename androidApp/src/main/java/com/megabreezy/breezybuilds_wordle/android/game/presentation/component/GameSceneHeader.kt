package com.megabreezy.breezybuilds_wordle.android.game.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.ui.image.ImageComponent
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.util.theme.ThemeFonts
import com.megabreezy.breezybuilds_wordle.android.util.theme.dpToSp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object GameSceneHeader
{
    val TextStyleKey = SemanticsPropertyKey<TextStyle>(name = "TextStyle")

    private var SemanticsPropertyReceiver.gameSceneHeaderTextStyle by TextStyleKey

    @Composable
    fun Component(options: ComponentOptions = ComponentOptions())
    {
        val textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onPrimary,
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier,
                verticalArrangement = Arrangement.Center
            )
            {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .width(width = LocalSceneDimensions.current.width * (370 / Scene.idealFrame().width))
                        .padding(bottom = LocalSceneDimensions.current.height * (10 / Scene.idealFrame().height))
                )
                {
                    options.gameOptions?.first()?.let { it() }

                    Text(
                        text = options.text,
                        modifier = Modifier.semantics { gameSceneHeaderTextStyle = textStyle },
                        style = textStyle
                    )

                    options.gameOptions?.forEachIndexed { index, option -> if (index > 0) option() }
                }

                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.error)
                        .fillMaxWidth()
                        .height(LocalSceneDimensions.current.height.times(1 / Scene.idealFrame().height))
                )
            }
        }
    }

    object Option: ImageComponent()
    {
        @Composable
        fun Component(resourceId: String, scope: CoroutineScope, onClick: suspend () -> Unit)
        {
            val imageResourceId = getDrawableResourceIdFromImageName(name = resourceId)

            Button(
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                onClick = { scope.launch { onClick() } }
            )
            {
                Image(
                    painter = painterResource(id = imageResourceId),
                    contentDescription = resourceId,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(LocalSceneDimensions.current.width.times(24 / Scene.idealFrame().width))
                        .aspectRatio(1f)
                )
            }
        }
    }

    data class ComponentOptions(val text: String = "", val gameOptions: (List<@Composable () -> Unit>)? = null)

    enum class TagName(private val id: String)
    {
        COMPONENT(id = "game_scene_header_component");

        override fun toString(): String = this.id
    }
}