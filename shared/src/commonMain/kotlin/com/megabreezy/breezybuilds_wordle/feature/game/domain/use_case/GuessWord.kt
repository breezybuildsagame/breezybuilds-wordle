package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotCreatedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessCreateFailedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer
import org.koin.core.component.inject

fun GameUseCase.guessWord()
{
    val guessRepository: GameGuessGateway by inject()

    try
    {
        val answer = GameUseCase().getGameAnswer()

        if (GameUseCase().getGameBoard().activeRow()?.firstOrNull { it.letter() == null } != null)
        {
            var invalidGuess = ""
            GameUseCase().getGameBoard().activeRow()?.forEach()
            {
                tile -> tile.letter()?.let { letter -> invalidGuess += letter }
            }
            throw GameUseCase.GuessWordInvalidGuessException("Invalid guess: $invalidGuess")
        }

        val guess = guessRepository.create()

        if (guess.word().isEmpty() || guess.word().count() < (GameUseCase().getGameBoard().activeRow()?.count() ?: 0))
        {
            throw GameUseCase.GuessWordInvalidGuessException("Invalid guess: ${guess.word()}")
        }

        if (guess.word() != answer.word())
        {
            throw GameUseCase.GuessWordFailedMismatchException(
                message = "Guess (${guess.word()}) does not match Answer (${answer.word()})."
            )
        }
    }
    catch (e: GameAnswerNotCreatedRepositoryException)
    {
        throw GameUseCase.GuessWordFailedException(message = e.message)
    }
    catch (e: GameGuessCreateFailedRepositoryException)
    {
        throw GameUseCase.GuessWordFailedNotInWordsListException(message = e.message)
    }
    catch (e: GameGuessNotFoundRepositoryException)
    {
        throw GameUseCase.GuessWordFailedNotInWordsListException(message = e.message)
    }
    catch (e: GameUseCase.GetGameAnswerFailedException)
    {
        throw GameUseCase.GuessWordFailedException(message = e.message)
    }
}