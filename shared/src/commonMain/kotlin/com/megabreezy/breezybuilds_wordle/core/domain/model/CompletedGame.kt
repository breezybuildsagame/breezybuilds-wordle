package com.megabreezy.breezybuilds_wordle.core.domain.model

import kotlinx.datetime.Clock

data class CompletedGame(
    private val answer: Answer,
    private val date: Long = Clock.System.now().epochSeconds,
    private val playerGuesses: List<Guess> = listOf()
)
{
    fun answer() = this.answer

    fun date() = this.date

    fun playerGuesses() = this.playerGuesses
}
