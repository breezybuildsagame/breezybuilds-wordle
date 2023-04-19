package com.megabreezy.breezybuilds_wordle.android.stats.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.util.theme.ThemeFonts
import com.megabreezy.breezybuilds_wordle.android.util.theme.dpToSp

object StatsModalContent
{
    @Composable
    fun Component(options: ComponentOptions = ComponentOptions())
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .semantics { contentDescription = "${TagName.CONTAINER}" }
        )
        {
            val titleTextStyle = getTitleTextStyle()
            Text(
                text = "STATISTICS",
                modifier = Modifier
                    .semantics { statsModalContentTitleTextStyle = titleTextStyle },
                style = titleTextStyle
            )

            options.statsRow?.let { it() }

            options.guessDistribution?.let { it() }

            options.playAgainButton?.let { it() }
        }
    }

    val TitleTextStyleKey = SemanticsPropertyKey<TextStyle>("TitleTextStyle")

    private var SemanticsPropertyReceiver.statsModalContentTitleTextStyle by TitleTextStyleKey

    @Composable fun getTitleTextStyle() = TextStyle(
        color = MaterialTheme.colorScheme.onSurface,
        fontFamily = ThemeFonts.roboto,
        fontWeight = FontWeight.Black,
        fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(20 / Scene.idealFrame().height))
    )

    data class ComponentOptions(
        val statsRow: (@Composable () -> Unit)? = null,
        var guessDistribution: (@Composable () -> Unit)? = null,
        var playAgainButton: (@Composable () -> Unit)? = null
    )

    enum class TagName(private val id: String)
    {
        CONTAINER(id = "stats_modal_content_component"), ROW(id = "stats_modal_content_stat_row");

        override fun toString(): String = this.id
    }

    object Stat
    {
        @Composable
        fun Component(options: ComponentOptions = ComponentOptions())
        {
            Column(
                modifier = options.modifier
                    .padding(bottom = LocalSceneDimensions.current.height.times(15 / Scene.idealFrame().height))
                    .semantics { contentDescription = "${TagName.STAT}" }
            )
            {
                options.headline?.let()
                { headlineText ->
                    val headlineTextStyle = getTextStyle(isHeadline = true)
                    Text(
                        text = headlineText,
                        modifier = Modifier
                            .semantics { statsModalContentStatHeadlineTextStyle = headlineTextStyle },
                        style = headlineTextStyle
                    )
                }

                options.description?.let()
                { descriptionText ->
                    val descriptionTextStyle = getTextStyle()
                    Text(
                        text = descriptionText,
                        modifier = Modifier
                            .semantics { statsModalContentDescriptionTextStyle = descriptionTextStyle },
                        style = descriptionTextStyle
                    )
                }
            }
        }

        val HeadlineTextStyleKey = SemanticsPropertyKey<TextStyle>("HeadlineTextStyle")
        val DescriptionTextStyleKey = SemanticsPropertyKey<TextStyle>("DescriptionTextStyle")

        private var SemanticsPropertyReceiver.statsModalContentStatHeadlineTextStyle by HeadlineTextStyleKey
        private var SemanticsPropertyReceiver.statsModalContentDescriptionTextStyle by DescriptionTextStyleKey

        @Composable fun getTextStyle(isHeadline: Boolean = false): TextStyle
        {
            return if (isHeadline) TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = ThemeFonts.roboto,
                fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(40 / Scene.idealFrame().height)),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
            else TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = ThemeFonts.roboto,
                fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(14 / Scene.idealFrame().height)),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            )
        }

        data class ComponentOptions(
            val modifier: Modifier = Modifier,
            val headline: String? = null,
            val description: String? = null
        )

        enum class TagName(private val id: String)
        {
            STAT("stats_modal_content_component_stat");

            override fun toString(): String = this.id
        }
    }

    object PlayAgainButton
    {
        @Composable
        fun Component(options: ComponentOptions = ComponentOptions())
        {
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                shape = RoundedCornerShape(
                    corner = CornerSize(size = LocalSceneDimensions.current.height.times(4 / Scene.idealFrame().height))
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                modifier = Modifier
                    .width(LocalSceneDimensions.current.width.times(275 / Scene.idealFrame().width))
                    .aspectRatio(ratio = 275 / 50f)
                    .semantics { contentDescription = "${TagName.BUTTON}" },
                onClick = options.onClick
            )
            {
                options.label?.let()
                { labelText ->
                    Text(
                        color = MaterialTheme.colorScheme.surface,
                        text = labelText,
                        style = TextStyle(
                            fontFamily = ThemeFonts.roboto,
                            fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(14 / Scene.idealFrame().height)),
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
        }

        data class ComponentOptions(val label: String? = null, val onClick: () -> Unit = { })

        enum class TagName(private val id: String)
        {
            BUTTON(id = "stats_modal_component_play_again_button");

            override fun toString(): String = this.id
        }
    }
}