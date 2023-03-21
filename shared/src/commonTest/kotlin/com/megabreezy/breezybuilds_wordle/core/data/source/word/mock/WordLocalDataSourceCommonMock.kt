package com.megabreezy.breezybuilds_wordle.core.data.source.word.mock

import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word

class WordLocalDataSourceCommonMock: WordLocalDataManageable
{
    var excludingWords: List<Word> = listOf()
    var getShouldFail = false
    var wordToReturn: Word? = null

    override fun get(excludingWords: List<Word>): Word
    {
        if (getShouldFail) throw WordNotFoundLocalDataException(message = "Not found.")

        wordToReturn = Word(WordLocalDataSourceMock.mockWords.first())
        this.excludingWords = excludingWords

        return wordToReturn!!
    }

    override fun getAll(): List<Word> = listOf(wordToReturn!!)
}