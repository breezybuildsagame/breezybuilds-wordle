package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

data class GameGuess(
    private val word: String
)
{
    fun word(): String = this.word
}
