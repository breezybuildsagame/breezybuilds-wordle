package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class GameAnswerTests
{
    @Test
    fun `When initialized with word - word method returns the expected uppercase string value`()
    {
        // given
        val expectedWord = "Perfect"

        // when
        val sut = GameAnswer(word = expectedWord)

        // then
        assertEquals(expectedWord.uppercase(), sut.word())
    }
}