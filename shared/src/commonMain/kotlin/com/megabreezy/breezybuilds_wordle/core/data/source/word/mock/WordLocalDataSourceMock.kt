package com.megabreezy.breezybuilds_wordle.core.data.source.word.mock

import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word

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
}