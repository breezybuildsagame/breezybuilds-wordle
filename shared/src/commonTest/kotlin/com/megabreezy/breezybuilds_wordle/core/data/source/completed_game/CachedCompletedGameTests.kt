package com.megabreezy.breezybuilds_wordle.core.data.source.completed_game

import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.CompletedGame
import com.megabreezy.breezybuilds_wordle.core.domain.model.Guess
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals

class CachedCompletedGameTests
{
    @Test
    fun `When initialized with CompletedGame - word property returns expected word`()
    {
        // given
        val expectedAnswer = Answer(word = Word("TOAST"), playerGuessedCorrectly = true)

        // when
        val sut = CachedCompletedGame(game = CompletedGame(answer = expectedAnswer))

        // then
        assertEquals(expectedAnswer, sut.answer())
    }

    @Test
    fun `When initialized with CompletedGame - date property returns expected word`()
    {
        // given
        val expectedDate =  Clock.System.now().epochSeconds

        // when
        val sut = CachedCompletedGame(game = CompletedGame(answer = Answer(word = Word("")), date = expectedDate))

        // then
        assertEquals(expectedDate, sut.date)
    }

    @Test
    fun `When initialized with CompletedGame - playerGuesses property returns expected list`()
    {
        // given
        val expectedGuesses = listOf(
            Guess(word = Word("TASTE")),
            Guess(word = Word("TRITE")),
            Guess(word = Word("TOAST"))
        )

        // when
        val sut = CachedCompletedGame(game = CompletedGame(answer = Answer(word = Word("TOAST")), playerGuesses = expectedGuesses))

        // then
        assertEquals(expectedGuesses, sut.playerGuesses())
    }
}