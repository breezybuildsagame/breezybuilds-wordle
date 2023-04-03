package com.megabreezy.breezybuilds_wordle.feature.stats.domain.model

import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalContentRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.button.ButtonRepresentable

class StatsModal(
    private var closeButton: ButtonRepresentable = Button { },
    private val stats: List<Stat> = listOf(),
    private val guessDistribution: GuessDistribution = GuessDistribution()
): AppModalContentRepresentable
{
    fun stats(): List<Stat> = this.stats

    fun guessDistribution(): GuessDistribution = this.guessDistribution

    fun setCloseButton(newCloseButton: Button) { this.closeButton = newCloseButton }

    override fun closeButton(): ButtonRepresentable = closeButton

    override fun equals(other: Any?): Boolean = other is StatsModal
        && this.stats() == other.stats()
        && this.guessDistribution() == other.guessDistribution()

    override fun hashCode(): Int
    {
        var result = closeButton.hashCode()
        result = 31 * result + stats.hashCode()
        result = 31 * result + guessDistribution.hashCode()
        return result
    }

    class Button(private val onClick: suspend () -> Unit): ButtonRepresentable
    {
        override suspend fun click() = onClick()
    }
}