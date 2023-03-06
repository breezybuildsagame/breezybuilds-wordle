package com.megabreezy.breezybuilds_wordle.core.util

import com.megabreezy.breezybuilds_wordle.core.domain.model.Word

expect class FileReader()
{
    fun getWordsList(): List<Word>
}