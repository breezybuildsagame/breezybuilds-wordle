package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway

import com.megabreezy.breezybuilds_wordle.core.data.source.guess.GuessClearFailedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.guess.GuessLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.guess.GuessSaveFailedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessCreateFailedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameGuess
import com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case.GameUseCase
import com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case.getGameBoard
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameGuessRepository: GameGuessGateway, KoinComponent
{
    private val guessLocalDataSource: GuessLocalDataManageable by inject()
    private val wordLocalDataSource: WordLocalDataManageable by inject()

    override fun create(): GameGuess
    {
        val gameBoard = GameUseCase().getGameBoard()
        var currentGuess = ""

        gameBoard.activeRow()?.forEach()
        { tile ->
            tile.letter()?.let { currentGuess = "$currentGuess$it" }
        }

        val possibleWords = wordLocalDataSource.getAll()
        if (!possibleWords.contains(Word(word = currentGuess)))
        {
            throw GameGuessNotFoundRepositoryException(message = "Word not found in words list.")
        }

        try
        {
            guessLocalDataSource.save(newGuess = currentGuess)
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

        guessLocalDataSource.getAll().forEach { guesses.add(GameGuess(word = it.word().toString())) }

        return guesses
    }

    override fun clear() = try
    {
        guessLocalDataSource.clear()
    }
    catch (e: GuessClearFailedLocalDataException)
    {
        throw GameGuessCreateFailedRepositoryException(message = e.message)
    }
}