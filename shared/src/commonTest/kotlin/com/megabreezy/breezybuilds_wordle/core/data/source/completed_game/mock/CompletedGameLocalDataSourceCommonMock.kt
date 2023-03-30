package com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.mock

import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameNotSavedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.CompletedGame
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word

class CompletedGameLocalDataSourceCommonMock: CompletedGameLocalDataManageable
{
    var getCompletedGameToReturn: CompletedGame? = null

    var getShouldFail = false
    var getShouldFailErrorMessage = "No CompletedGame records found."

    var putGames: MutableList<CompletedGame> = mutableListOf()
    var putShouldFail = false
    var putShouldFailErrorMessage = "CompletedGame not saved."

    override fun get(): CompletedGame
    {
        if (getShouldFail) throw CompletedGameNotFoundLocalDataException(message = getShouldFailErrorMessage)

        getCompletedGameToReturn = getCompletedGameToReturn ?: CompletedGame(answer = Answer(word = Word("SLAYS")))

        return getCompletedGameToReturn!!
    }

    override suspend fun put(newCompletedGame: CompletedGame): CompletedGame
    {
        if (putShouldFail) throw CompletedGameNotSavedLocalDataException(message = putShouldFailErrorMessage)

        putGames.add(newCompletedGame)

        return newCompletedGame
    }
}