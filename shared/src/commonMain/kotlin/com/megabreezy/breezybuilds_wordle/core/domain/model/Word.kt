package com.megabreezy.breezybuilds_wordle.core.domain.model

data class Word(private val word: String)
{
    fun letters() = word.toCharArray()
    fun word() = word

    override fun toString(): String = this.word()

    override fun equals(other: Any?): Boolean = other is Word
        && this.word().trim().lowercase() == other.word().trim().lowercase()

    override fun hashCode(): Int = word.hashCode()
}
