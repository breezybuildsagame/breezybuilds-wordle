package com.megabreezy.breezybuilds_wordle.core.domain.model

import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class CompletedGameTests
{
    val mockAnswer = Answer(word = Word("SLAPS"))

    @Test
    fun `When entity initialized with date long value - date method returns expected value`()
    {
        // given
        val expectedDate = Clock.System.now().epochSeconds

        // when
        val sut = CompletedGame(answer = mockAnswer)

        // then
        assertEquals(expectedDate, sut.date())
    }

    @Test
    fun `When entity initialized with answer - answer method returns expected value`()
    {
        // given
        val expectedAnswer = mockAnswer

        // when
        val sut = CompletedGame(answer = expectedAnswer)

        // then
        assertEquals(expectedAnswer, sut.answer())
    }

    @Test
    fun `When entity initialized with playerGuesses Guess list - playerGuesses method returns expected list`()
    {
        // given
        val expectedGuesses = listOf(
            Guess(word = Word("GUESS1")),
            Guess(word = Word("GUESS2"))
        )

        // when
        val sut = CompletedGame(answer = mockAnswer, playerGuesses = expectedGuesses)

        // then
        assertEquals(expectedGuesses, sut.playerGuesses())
    }
}