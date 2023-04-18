package com.megabreezy.breezybuilds_wordle.android.stats.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.util.theme.ThemeFonts
import com.megabreezy.breezybuilds_wordle.android.util.theme.dpToSp

object StatsGuessDistribution
{
    object GraphRow
    {
        @Composable
        fun Component(options: ComponentOptions = ComponentOptions())
        {
            val textStyle = getRowTextStyle()

            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    LocalSceneDimensions.current.width.times(5 / Scene.idealFrame().width)
                ),
                modifier = Modifier
                    .semantics()
                    {
                        statsGuessDistributionGraphRowTextStyle = textStyle
                        contentDescription = "${TagName.ROW}"
                    },
                verticalAlignment = Alignment.CenterVertically
            )
            {
                options.round?.let()
                { roundText ->
                    Text(
                        text = roundText,
                        style = textStyle
                    )
                }

                options.correctGuessCount?.let()
                { correctGuessCount ->
                    Text(
                        text = correctGuessCount,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.error)
                            .padding(
                                horizontal = LocalSceneDimensions.current.width.times(5 / Scene.idealFrame().width)
                            ),
                        style = textStyle
                    )
                }
            }
        }

        @Composable
        fun getRowTextStyle() = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = ThemeFonts.roboto,
            fontWeight = FontWeight.Normal,
            fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(20 / Scene.idealFrame().height)),
            textAlign = TextAlign.Right
        )

        val TextStyleKey = SemanticsPropertyKey<TextStyle>(name = "TextStyle")

        private var SemanticsPropertyReceiver.statsGuessDistributionGraphRowTextStyle by TextStyleKey

        data class ComponentOptions(val round: String? = null, val correctGuessCount: String? = null)

        enum class TagName(private val id: String)
        {
            ROW(id = "stats_guess_distribution_component_row");

            override fun toString(): String = this.id
        }
    }
}