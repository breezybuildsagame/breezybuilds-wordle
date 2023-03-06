package com.megabreezy.breezybuilds_wordle.core.util

import android.content.Context
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.library.MR

actual class FileReader
{
    var context: Context? = null

    actual fun getWordsList(): List<Word>
    {
        context?.let()
        { context ->
            val wordsList = MR.files.words.readText(context = context).trim().split("[\r\n]+".toRegex()).toTypedArray()
            return wordsList.toList().map { Word(word = it) }
        }

        return listOf()
    }
}