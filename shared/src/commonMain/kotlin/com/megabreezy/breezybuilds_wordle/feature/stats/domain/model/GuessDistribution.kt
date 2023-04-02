package com.megabreezy.breezybuilds_wordle.feature.stats.domain.model

data class GuessDistribution(
    private val title: String = "",
    private val round: Int = 0,
    private val correctGuessesCount: Int = 0
)
{
    fun title() = this.title

    fun round() = this.round

    fun correctGuessesCount() = this.correctGuessesCount
}
