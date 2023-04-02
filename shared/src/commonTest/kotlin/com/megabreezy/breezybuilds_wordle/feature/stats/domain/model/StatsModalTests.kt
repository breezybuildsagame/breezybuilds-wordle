package com.megabreezy.breezybuilds_wordle.feature.stats.domain.model

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class StatsModalTests
{
    @Test
    fun `When entity initialized with stats parameter - stats method returns expected stats list`()
    {
        // given
        val expectedStatsList = listOf(
            Stat(headline = "10", description = "Number of Ws"),
            Stat(headline = "9", description = "Number of Ls")
        )

        // when
        val sut = StatsModal(stats = expectedStatsList)

        // then
        assertEquals(expectedStatsList, sut.stats())
    }

    @Test
    fun `When entity initialized with guessDistribution parameter - guessDistribution method returns expected entity`()
    {
        // given
        val expectedGuessDistribution = GuessDistribution(
            title = "Guess Distribution",
            rows = listOf(
                GuessDistribution.Row(round = 1, correctGuessesCount = 10),
                GuessDistribution.Row(round = 2, correctGuessesCount = 3)
            )
        )

        // when
        val sut = StatsModal(guessDistribution = expectedGuessDistribution)

        // then
        assertEquals(expectedGuessDistribution, sut.guessDistribution())
    }

    @Test
    fun `When entity initialized with closeButton - closeButton method returns expected button representable`()
    {
        // given
        var clickDidInvoke = false
        val mockButton = StatsModal.Button { clickDidInvoke = true }

        // when
        val sut = StatsModal(closeButton = mockButton)
        runBlocking { sut.closeButton().click() }

        // then
        assertTrue(clickDidInvoke)
    }
}