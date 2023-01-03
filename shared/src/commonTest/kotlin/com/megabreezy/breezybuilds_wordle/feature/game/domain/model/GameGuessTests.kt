package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class GameGuessTests
{
    @Test
    fun `When initialized with word - word method returns the expected uppercase string value`()
    {
        // given
        val expectedWord = "SLICE"

        // when
        val sut = GameGuess(word = expectedWord)

        // then
        assertEquals(expectedWord, sut.word())
    }
}