package com.megabreezy.breezybuilds_wordle.android.help.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.ui.tile.CoreTile
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
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
            Column(
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
            }
        }

        data class ComponentOptions(
            val tiles: List<@Composable () -> Unit> = listOf()
        )
    }

    enum class TagName(private val id: String)
    {
        CONTENT(id = "help_sheet_content"),
        TILE(id = "help_sheet_tile"),
        EXAMPLE(id = "help_sheet_example");

        override fun toString(): String = this.id
    }
}