package com.megabreezy.breezybuilds_wordle.core.data.source.guess.mock

import com.megabreezy.breezybuilds_wordle.core.data.source.guess.GuessClearFailedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.guess.GuessLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.guess.GuessSaveFailedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.domain.model.Guess
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import org.koin.core.module.Module

class GuessLocalDataSourceMock(
    private val guessesToReturn: List<Guess> = listOf(),
    val saveErrorMessage: String? = null,
    val clearErrorMessage: String? = null
): GuessLocalDataManageable
{
    override fun getAll(): List<Guess> = guessesToReturn

    override fun save(newGuess: String): Guess
    {
        saveErrorMessage?.let { throw GuessSaveFailedLocalDataException(message = it) }

        return Guess(word = Word(newGuess))
    }

    override fun clear()
    {
        clearErrorMessage?.let { throw GuessClearFailedLocalDataException(message = it) }
    }

    companion object
    {
        val guesses = listOf(
            Guess(word = Word(word = "SNAIL")),
            Guess(word = Word(word = "WHOLE")),
            Guess(word = Word(word = "SPEAK")),
            Guess(word = Word(word = "FRESH")),
        )

        fun injectDefinition(module: Module, scenarios: List<Scenario>)
        {
            val guessesFound = scenarios.firstOrNull { it == Scenario.GUESSES_FOUND } != null
            val guessNotSaved = scenarios.firstOrNull { it == Scenario.GUESS_NOT_SAVED } != null

            module.single<GuessLocalDataManageable>()
            {
                GuessLocalDataSourceMock(
                    guessesToReturn = if (guessesFound) guesses else listOf(),
                    saveErrorMessage = if (guessNotSaved) "Failed to save new Guess." else null,
                    clearErrorMessage = if (guessNotSaved) "Failed to clear all Guesses." else null
                )
            }
        }
    }
}