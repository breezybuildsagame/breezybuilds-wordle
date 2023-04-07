package com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.mock

import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameNotSavedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.CompletedGame
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word

class CompletedGameLocalDataSourceCommonMock: CompletedGameLocalDataManageable
{
    var getCompletedGamesToReturn: MutableList<CompletedGame> = mutableListOf()

    var getShouldFail = false
    var getShouldFailErrorMessage = "No CompletedGame records found."

    var putGames: MutableList<CompletedGame> = mutableListOf()
    var putShouldFail = false
    var putShouldFailErrorMessage = "CompletedGame not saved."

    override fun getAll(): List<CompletedGame>
    {
        if (getShouldFail) throw CompletedGameNotFoundLocalDataException(message = getShouldFailErrorMessage)

        if (getCompletedGamesToReturn.isEmpty())
        {
            getCompletedGamesToReturn.add(element = CompletedGame(answer = Answer(word = Word("SLAYS"))))
        }

        return getCompletedGamesToReturn
    }

    override suspend fun put(newCompletedGame: CompletedGame): CompletedGame
    {
        if (putShouldFail) throw CompletedGameNotSavedLocalDataException(message = putShouldFailErrorMessage)

        putGames.add(newCompletedGame)
        getCompletedGamesToReturn.add(newCompletedGame)

        return newCompletedGame
    }
}