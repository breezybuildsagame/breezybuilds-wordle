package com.megabreezy.breezybuilds_wordle.core.domain.model

data class Guess(
    private val word: Word
)
{
    fun word(): Word = this.word
}