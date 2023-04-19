package com.megabreezy.breezybuilds_wordle.android.stats.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.util.theme.ThemeFonts
import com.megabreezy.breezybuilds_wordle.android.util.theme.dpToSp

object StatsModalContent
{

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
}