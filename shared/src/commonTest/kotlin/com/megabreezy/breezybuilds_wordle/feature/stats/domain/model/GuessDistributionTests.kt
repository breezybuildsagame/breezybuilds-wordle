package com.megabreezy.breezybuilds_wordle.feature.stats.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class GuessDistributionTests
{
    @Test
    fun `When entity initialized with title parameter - title method returns expected string value`()
    {
        // given
        val expectedTitle = "Guess Distribution"

        // when
        val sut = GuessDistribution(title = expectedTitle)

        // then
        assertEquals(expectedTitle, sut.title())
    }

    @Test
    fun `When entity initialized with round parameter - title method returns expected integer value`()
    {
        // given
        val expectedRound = 9

        // when
        val sut = GuessDistribution(round = expectedRound)

        // then
        assertEquals(expectedRound, sut.round())
    }

    @Test
    fun `When entity initialized with correctGuessesCount parameter - correctGuessesCount method returns expected integer value`()
    {
        // given
        val expectedCorrectGuessesCount = 39

        // when
        val sut = GuessDistribution(correctGuessesCount = expectedCorrectGuessesCount)

        // then
        assertEquals(expectedCorrectGuessesCount, sut.correctGuessesCount())
    }
}