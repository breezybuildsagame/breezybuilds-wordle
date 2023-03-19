package com.megabreezy.breezybuilds_wordle.core.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WordTests
{
    @Test
    fun `When initialized with word - word method returns expected string`()
    {
        // given
        val expectedWord = "basketball"

        // when
        val sut = Word(word = expectedWord)

        // then
        assertEquals(expectedWord, sut.word())
    }

    @Test
    fun `When searching for a capitalized word in a list of lowercase words - expected word is found`()
    {
        // given
        val words = listOf(
            Word(word = "test"),
            Word(word = "another"),
            Word(word = "finally")
        )

        // when
        val wordIsFound = words.contains(Word(word = "FINALLY"))

        // then
        assertTrue(wordIsFound)
    }

    @Test
    fun `When initialized with word - letters method returns expected list of characters`()
    {
        // given
        val fullWord = "candy"
        val expectedLetters= CharArray(size = 5)
        fullWord.forEachIndexed()
        { index, char ->
            expectedLetters[index] = char
        }

        // when
        val sut = Word(word = fullWord)

        // then
        assertTrue(expectedLetters contentEquals sut.letters())
    }
}