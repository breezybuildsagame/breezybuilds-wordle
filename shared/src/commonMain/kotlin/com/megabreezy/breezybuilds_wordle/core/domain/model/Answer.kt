package com.megabreezy.breezybuilds_wordle.core.domain.model

data class Answer(
    private val word: Word,
    private var isCurrent: Boolean = false,
    private var playerGuessedCorrectly: Boolean? = null
)
{
    fun isCurrent() = this.isCurrent

    fun playerGuessedCorrectly(): Boolean? = this.playerGuessedCorrectly

    fun setIsCurrent(newIsCurrent: Boolean)
    {
        this.isCurrent = newIsCurrent
    }

    fun setPlayerGuessedCorrectly(newPlayerGuessedCorrectly: Boolean?)
    {
        this.playerGuessedCorrectly = newPlayerGuessedCorrectly
    }

    fun word(): Word = this.word
}
