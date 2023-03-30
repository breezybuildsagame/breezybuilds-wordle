package com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.mock

import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameNotSavedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.CompletedGame
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import org.koin.core.module.Module

class CompletedGameLocalDataSourceMock(
    private val completedGamesToReturn: MutableList<CompletedGame> = mutableListOf(),
    val getErrorMessage: String? = null,
    val putErrorMessage: String? = null
): CompletedGameLocalDataManageable
{
    override fun getAll(): List<CompletedGame>
    {
        getErrorMessage?.let { throw CompletedGameNotFoundLocalDataException(message = getErrorMessage) }

        return completedGamesToReturn.toList()
    }

    override suspend fun put(newCompletedGame: CompletedGame): CompletedGame
    {
        putErrorMessage?.let { throw CompletedGameNotSavedLocalDataException(message = putErrorMessage) }

        completedGamesToReturn.add(newCompletedGame)

        return newCompletedGame
    }

    companion object
    {
        fun injectDefinition(module: Module, scenarios: List<Scenario>)
        {
            val gamesFound = scenarios.firstOrNull { it == Scenario.COMPLETED_GAMES_FOUND } != null
            val gameNotSaved = scenarios.firstOrNull { it == Scenario.COMPLETED_GAME_NOT_SAVED } != null

            module.single<CompletedGameLocalDataManageable>()
            {
                CompletedGameLocalDataSourceMock(
                    completedGamesToReturn = if (gamesFound) mutableListOf(
                            CompletedGame(answer = Answer(word = Word("SLAYS"))),
                            CompletedGame(answer = Answer(word = Word("TEST")))
                        ) else mutableListOf(),
                    getErrorMessage = if (gamesFound) null else "No CompletedGame records found.",
                    putErrorMessage = if (gameNotSaved) "CompletedGame save failed." else null
                )
            }
        }
    }
}