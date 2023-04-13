package com.megabreezy.breezybuilds_wordle.android.stats.presentation.component

import android.nfc.Tag
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics

object StatsGuessDistribution
{
    object GraphRow
    {
        @Composable
        fun Component()
        {
            Row(
                modifier = Modifier
                    .semantics { contentDescription = "${TagName.ROW}" }
            )
            {

            }
        }

        enum class TagName(private val id: String)
        {
            ROW(id = "stats_guess_distribution_component_row");

            override fun toString(): String = this.id
        }
    }
}