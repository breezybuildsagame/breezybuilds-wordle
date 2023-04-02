package com.megabreezy.breezybuilds_wordle.feature.stats.domain.model

import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalContentRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.button.ButtonRepresentable

class StatsModal(
    private val closeButton: ButtonRepresentable = Button { },
    private val stats: List<Stat> = listOf(),
    private val guessDistribution: GuessDistribution = GuessDistribution()
): AppModalContentRepresentable
{
    fun stats(): List<Stat> = this.stats

    fun guessDistribution(): GuessDistribution = this.guessDistribution

    override fun closeButton(): ButtonRepresentable = closeButton

    class Button(private val onClick: suspend () -> Unit): ButtonRepresentable
    {
        override suspend fun click() = onClick()
    }
}