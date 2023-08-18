package com.megabreezy.breezybuilds_wordle.android.help.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.ui.tile.CoreTile
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.util.theme.ThemeFonts
import com.megabreezy.breezybuilds_wordle.android.util.theme.dpToSp
import com.megabreezy.breezybuilds_wordle.feature.help.domain.model.HelpSheet

object HelpSheetComposable
{
    @Composable
    fun Component()
    {

    }

    object Tile
    {
        @Composable
        fun Component(options: ComponentOptions = ComponentOptions())
        {
            CoreTile.Component(
                options = CoreTile.ComponentOptions(
                    letter = options.letter,
                    modifier = Modifier
                        .semantics { contentDescription = "${TagName.TILE}" },
                    state = options.state.name,
                    tileSize = 50f
                )
            )
        }

        data class ComponentOptions(
            val state: HelpSheet.Tile.State = HelpSheet.Tile.State.HIDDEN,
            val letter: String = ""
        )
    }

    object Example
    {
        @Composable
        fun Component(options: ComponentOptions = ComponentOptions())
        {
            val descriptionTextStyle = TextStyle(
                color = MaterialTheme.colorScheme.onBackground,
                fontFamily = ThemeFonts.roboto,
                fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(15 / Scene.idealFrame().height)),
                fontWeight = FontWeight.Normal
            )

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .semantics { contentDescription = "${TagName.EXAMPLE}" },
                verticalArrangement = Arrangement.spacedBy(
                    LocalSceneDimensions.current.height.times(15 / Scene.idealFrame().height))
            )
            {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(LocalSceneDimensions.current.width.times(6 / Scene.idealFrame().width)),
                    modifier = Modifier
                        .semantics { contentDescription = "ROW" }
                )
                {
                    options.tiles.forEach { it() }
                }

                Row(
                    modifier = Modifier
                        .semantics { contentDescription = "ROW" }
                )
                {
                    Text(
                        text = options.description,
                        modifier = Modifier
                            .semantics { helpSheetExampleDescriptionTextStyle = descriptionTextStyle },
                        style = descriptionTextStyle
                    )

                    Scene().ColumnSpacer(modifier = Modifier.weight(1f))
                }
            }
        }

        data class ComponentOptions(
            val tiles: List<@Composable () -> Unit> = listOf(),
            val description: String = ""
        )

        val DescriptionTextStyleKey = SemanticsPropertyKey<TextStyle>("DescriptionTextStyle")

        private var SemanticsPropertyReceiver.helpSheetExampleDescriptionTextStyle by DescriptionTextStyleKey
    }

    enum class TagName(private val id: String)
    {
        CONTENT(id = "help_sheet_content"),
        TILE(id = "help_sheet_tile"),
        EXAMPLE(id = "help_sheet_example");

        override fun toString(): String = this.id
    }
}