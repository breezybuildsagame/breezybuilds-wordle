package com.megabreezy.breezybuilds_wordle.core.data.source.word.mock

import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import org.koin.core.module.Module

class WordLocalDataSourceMock(
    private val wordToReturn: Word? = null,
    private val getErrorMessage: String = "Failed to get Word."
): WordLocalDataManageable
{
    override fun get(): Word
    {
        wordToReturn?.let { return it }

        throw WordNotFoundLocalDataException(message = getErrorMessage)
    }

    companion object
    {
        val mockWords = listOf(
            "ALPHA", "FIRST", "PLACE", "SHAPE", "FLOAT"
        )

        fun injectDefinition(module: Module, scenarios: List<Scenario>)
        {
            val wordWasFound =  scenarios.firstOrNull { it == Scenario.WORD_FOUND } != null

            module.single<WordLocalDataManageable>()
            {
                WordLocalDataSourceMock(
                    wordToReturn = if (wordWasFound) Word(word = mockWords.random()) else null
                )
            }
        }
    }
}