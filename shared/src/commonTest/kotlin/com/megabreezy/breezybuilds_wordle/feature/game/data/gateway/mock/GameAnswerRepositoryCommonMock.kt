package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.mock

import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotCreatedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotUpdatedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer
import kotlinx.coroutines.runBlocking

class GameAnswerRepositoryCommonMock: GameAnswerGateway
{
    var createdGameAnswer: GameAnswer? = null
    var createShouldFail = false
    var updatedGameAnswerToReturn: GameAnswer? = null
    var gameAnswer: GameAnswer? = null
    var getShouldFail = false
    var guessMatchesAnswer = false
    var guessContainsCloseLetter = false
    var updateShouldFail = false
    var updateAnswerGuessedDidInvoke = false
    var updateAnswerNotGuessedDidInvoke = false

    var createExceptionMessage = "Answer not created."
    var getExceptionMessage = "Answer not found."
    var updateExceptionMessage = "Answer not updated"

    override suspend fun create(): GameAnswer
    {
        if (createShouldFail) throw GameAnswerNotCreatedRepositoryException(createExceptionMessage)

        createdGameAnswer?.let { return it }

        createdGameAnswer = if (guessMatchesAnswer) GameAnswer(word = "PLAYS")
        else if (guessContainsCloseLetter) GameAnswer(word = "SPEAR")
        else GameAnswer(word = "TESTS")

        return createdGameAnswer!!
    }

    override fun get(): GameAnswer
    {
        if (getShouldFail) throw GameAnswerNotFoundRepositoryException(getExceptionMessage)

        gameAnswer = gameAnswer ?: createdGameAnswer ?: GameAnswer(word = "SLAYS")

        return gameAnswer!!
    }

    override suspend fun updateAnswerGuessed(existingAnswer: GameAnswer): GameAnswer
    {
        if (updateShouldFail) throw GameAnswerNotUpdatedRepositoryException(message = updateExceptionMessage)
        updatedGameAnswerToReturn = existingAnswer
        updateAnswerGuessedDidInvoke = true

        return existingAnswer
    }

    override suspend fun updateAnswerNotGuessed(existingAnswer: GameAnswer): GameAnswer
    {
        if (updateShouldFail) throw GameAnswerNotUpdatedRepositoryException(message = updateExceptionMessage)
        updatedGameAnswerToReturn = existingAnswer
        updateAnswerNotGuessedDidInvoke = true

        return existingAnswer
    }
}