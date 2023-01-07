package com.megabreezy.breezybuilds_wordle.core.domain.model

data class Answer(
    private val word: Word,
    private var isCurrent: Boolean = false
)
{
    fun isCurrent() = this.isCurrent

    fun setIsCurrent(newIsCurrent: Boolean)
    {
        this.isCurrent = newIsCurrent
    }

    fun word(): Word = this.word
}
