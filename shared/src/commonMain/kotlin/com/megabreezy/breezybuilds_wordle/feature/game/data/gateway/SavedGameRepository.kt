package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway

import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameNotSavedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.CompletedGame
import com.megabreezy.breezybuilds_wordle.core.domain.model.Guess
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.*
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameGuess
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.SavedGame
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SavedGameRepository: SavedGameGateway, KoinComponent
{
    private val completedGameLocalDataSource: CompletedGameLocalDataManageable by inject()
    private val gameAnswerRepository: GameAnswerGateway by inject()
    private val gameGuessRepository: GameGuessGateway by inject()

    override suspend fun create(): SavedGame
    {
        try
        {
            val currentAnswer = gameAnswerRepository.get()
            val guesses = gameGuessRepository.getAll()

            completedGameLocalDataSource.put(
                newCompletedGame = CompletedGame(
                    answer = Answer(
                        word = Word(currentAnswer.word()),
                        isCurrent = false,
                        playerGuessedCorrectly = guesses.contains(GameGuess(word = currentAnswer.word()))
                    ),
                    date = Clock.System.now().epochSeconds,
                    playerGuesses = guesses.map { Guess(word = Word(it.word())) }
                )
            )

            return SavedGame()
        }
        catch(e: Throwable)
        {
            when (e)
            {
                is GameAnswerNotFoundRepositoryException ->
                    throw SavedGameCreateFailedRepositoryException(message = e.message)
                is CompletedGameNotSavedLocalDataException ->
                    throw SavedGameCreateFailedRepositoryException(message = e.message)
                else -> throw e
            }
        }
    }
}