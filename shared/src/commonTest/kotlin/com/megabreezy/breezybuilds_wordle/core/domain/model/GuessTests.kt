package com.megabreezy.breezybuilds_wordle.core.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class GuessTests
{
    @Test
    fun `when initialized with word - word method returns expected word value`()
    {
        // given
        val expectedWord = Word(word = "SPACE")

        // when
        val sut = Guess(word = expectedWord)

        // then
        assertEquals(expectedWord, sut.word())
    }
}