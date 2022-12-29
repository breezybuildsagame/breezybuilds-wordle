package com.megabreezy.breezybuilds_wordle.core.data.source.word

import com.megabreezy.breezybuilds_wordle.core.domain.model.Word

interface WordLocalDataManageable
{
    fun get(): Word
}

class WordNotFoundLocalDataException(message: String): Exception(message)