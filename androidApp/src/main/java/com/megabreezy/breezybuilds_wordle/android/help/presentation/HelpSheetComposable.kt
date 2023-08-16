package com.megabreezy.breezybuilds_wordle.android.help.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.megabreezy.breezybuilds_wordle.android.core.ui.tile.CoreTile

object HelpSheetComposable
{
    @Composable
    fun Component()
    {

    }

    object Tile
    {
        @Composable
        fun Component()
        {
            CoreTile.Component(
                options = CoreTile.ComponentOptions(
                    modifier = Modifier
                        .semantics { contentDescription = "${TagName.TILE}" }
                )
            )
        }
    }

    enum class TagName(private val id: String)
    {
        CONTENT(id = "help_sheet_content"),
        TILE(id = "help_sheet_tile");

        override fun toString(): String = this.id
    }
}