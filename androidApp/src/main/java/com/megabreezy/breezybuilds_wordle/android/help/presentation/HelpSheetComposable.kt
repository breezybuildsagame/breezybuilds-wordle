package com.megabreezy.breezybuilds_wordle.android.help.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.megabreezy.breezybuilds_wordle.android.core.ui.tile.CoreTile
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

    enum class TagName(private val id: String)
    {
        CONTENT(id = "help_sheet_content"),
        TILE(id = "help_sheet_tile");

        override fun toString(): String = this.id
    }
}