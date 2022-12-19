package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

data class GameAnswer(
    private val word: String = ""
)
{
    fun word() = word.uppercase()
}
