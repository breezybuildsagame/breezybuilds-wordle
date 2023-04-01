package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.mock

import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotUpdatedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer
import kotlinx.coroutines.runBlocking

class GameAnswerRepositoryCommonMock: GameAnswerGateway
{
    var createdGameAnswer: GameAnswer? = null
    var updatedGameAnswerToReturn: GameAnswer? = null
    var gameAnswer: GameAnswer? = null
    var getShouldFail = false
    var guessMatchesAnswer = false
    var guessContainsCloseLetter = false
    var updateShouldFail = false
    var updateAnswerGuessedDidInvoke = false
    var updateAnswerNotGuessedDidInvoke = false

    override suspend fun create(): GameAnswer
    {
        createdGameAnswer = if (guessMatchesAnswer) GameAnswer(word = "PLAYS")
        else if (guessContainsCloseLetter) GameAnswer(word = "SPEAR")
        else GameAnswer(word = "TESTS")

        return createdGameAnswer!!
    }

    override fun get(): GameAnswer = createdGameAnswer?.let()
    {
        if (getShouldFail) throw GameAnswerNotFoundRepositoryException("Answer not found.")

        gameAnswer = createdGameAnswer

        return gameAnswer!!
    } ?: runBlocking { create() }

    override suspend fun updateAnswerGuessed(existingAnswer: GameAnswer): GameAnswer
    {
        if (updateShouldFail) throw GameAnswerNotUpdatedRepositoryException(message = "Answer not updated")
        updatedGameAnswerToReturn = existingAnswer
        updateAnswerGuessedDidInvoke = true

        return existingAnswer
    }

    override suspend fun updateAnswerNotGuessed(existingAnswer: GameAnswer): GameAnswer
    {
        if (updateShouldFail) throw GameAnswerNotUpdatedRepositoryException(message = "Answer not updated")
        updatedGameAnswerToReturn = existingAnswer
        updateAnswerNotGuessedDidInvoke = true

        return existingAnswer
    }
}