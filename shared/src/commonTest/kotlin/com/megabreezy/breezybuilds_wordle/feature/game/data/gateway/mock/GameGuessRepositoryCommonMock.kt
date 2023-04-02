package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.mock

import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameGuess

class GameGuessRepositoryCommonMock: GameGuessGateway
{
    var guessIsInvalid = false
    var guessNotFound = false
    var guessContainsMatchingLetters = false
    var guessToReturn: GameGuess? = null
    var getAllGuessesToReturn: List<GameGuess>? = null
    var getAllShouldFail = false
    var clearDidInvoke = false

    override suspend fun create(): GameGuess
    {
        if (guessNotFound) throw GameGuessNotFoundRepositoryException("Not found in words list.")

        guessToReturn = if (guessIsInvalid) GameGuess(word = "T")
        else if (guessContainsMatchingLetters) GameGuess(word = "TREAT")
        else GameGuess(word = "PLAYS")

        return guessToReturn!!
    }

    override fun getAll(): List<GameGuess>
    {
        if (getAllShouldFail) throw GameGuessNotFoundRepositoryException("No guesses found.")

        getAllGuessesToReturn = getAllGuessesToReturn ?: listOf(
            GameGuess(word = "TRIES"),
            GameGuess(word = "SLAPS"),
            GameGuess(word = "GLIDE")
        )

        return getAllGuessesToReturn!!
    }

    override suspend fun clear() { clearDidInvoke = true }
}