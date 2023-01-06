package com.megabreezy.breezybuilds_wordle.core.data.source.word

import com.megabreezy.breezybuilds_wordle.core.domain.model.Word

interface WordLocalDataManageable
{
    fun get(excludingWords: List<Word> = listOf()): Word
    fun getAll(): List<Word>
}

class WordNotFoundLocalDataException(message: String): Exception(message)