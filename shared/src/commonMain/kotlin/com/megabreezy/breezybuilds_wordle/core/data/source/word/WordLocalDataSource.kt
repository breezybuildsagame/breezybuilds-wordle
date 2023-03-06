package com.megabreezy.breezybuilds_wordle.core.data.source.word

import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.util.FileReader

class WordLocalDataSource: WordLocalDataManageable
{
    var fileReader = FileReader()

    override fun get(excludingWords: List<Word>): Word
    {
        val availableWords = getAll()
        val unusedWords = availableWords - excludingWords.toSet()

        return unusedWords.random()
    }

    override fun getAll(): List<Word>
    {
        return fileReader.getWordsList()
    }
}