package com.megabreezy.breezybuilds_wordle.core.util

import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.library.MR

actual class FileReader
{
    actual fun getWordsList(): List<Word>
    {
        val wordsList = MR.files.words.readText().trim().split("[\r\n]+".toRegex()).toTypedArray()

        return wordsList.toList().map { Word(word = it) }
    }
}