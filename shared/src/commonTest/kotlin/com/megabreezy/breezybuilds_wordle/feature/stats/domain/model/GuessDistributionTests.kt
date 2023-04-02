package com.megabreezy.breezybuilds_wordle.feature.stats.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class GuessDistributionTests
{
    @Test
    fun `When Row initialized with round parameter - title method returns expected integer value`()
    {
        // given
        val expectedRound = 9

        // when
        val sut = GuessDistribution.Row(round = expectedRound)

        // then
        assertEquals(expectedRound, sut.round())
    }

    @Test
    fun `When Row initialized with correctGuessesCount parameter - correctGuessesCount method returns expected integer value`()
    {
        // given
        val expectedCorrectGuessesCount = 39

        // when
        val sut = GuessDistribution.Row(correctGuessesCount = expectedCorrectGuessesCount)

        // then
        assertEquals(expectedCorrectGuessesCount, sut.correctGuessesCount())
    }

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
    fun `When entity initialized with rows parameter - rows method returns expected rows list`()
    {
        // given
        val expectedRows = listOf(
            GuessDistribution.Row(round = 1, correctGuessesCount = 10),
            GuessDistribution.Row(round = 2, correctGuessesCount = 3)
        )

        // when
        val sut = GuessDistribution(rows = expectedRows)

        // then
        assertEquals(expectedRows, sut.rows())
    }
}