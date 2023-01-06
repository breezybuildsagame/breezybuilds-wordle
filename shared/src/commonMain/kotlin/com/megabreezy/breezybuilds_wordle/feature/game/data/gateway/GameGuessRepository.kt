package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway

import com.megabreezy.breezybuilds_wordle.core.data.source.guess.GuessClearFailedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.guess.GuessLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.guess.GuessSaveFailedLocalDataException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessCreateFailedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameGuess
import com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case.GameUseCase
import com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case.getGameBoard
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameGuessRepository: GameGuessGateway, KoinComponent
{
    private val localDataSource: GuessLocalDataManageable by inject()

    override fun create(): GameGuess
    {
        val gameBoard = GameUseCase().getGameBoard()
        var currentGuess = ""

        gameBoard.activeRow()?.forEach()
        { tile ->
            tile.letter()?.let { currentGuess = "$currentGuess$it" }
        }

        try
        {
            localDataSource.save(newGuess = currentGuess)
        }
        catch(e: GuessSaveFailedLocalDataException)
        {
            throw GameGuessCreateFailedRepositoryException(e.message)
        }

        return GameGuess(word = currentGuess)
    }

    override fun getAll(): List<GameGuess>
    {
        val guesses = mutableListOf<GameGuess>()

        localDataSource.getAll().forEach { guesses.add(GameGuess(word = it.word().toString())) }

        return guesses
    }

    override fun clear() = try
    {
        localDataSource.clear()
    }
    catch (e: GuessClearFailedLocalDataException)
    {
        throw GameGuessCreateFailedRepositoryException(message = e.message)
    }
}