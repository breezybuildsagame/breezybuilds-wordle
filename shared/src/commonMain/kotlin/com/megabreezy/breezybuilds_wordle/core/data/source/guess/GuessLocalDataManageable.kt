package com.megabreezy.breezybuilds_wordle.core.data.source.guess

import com.megabreezy.breezybuilds_wordle.core.domain.model.Guess

interface GuessLocalDataManageable
{
    fun getAll(): List<Guess>
    fun save(newGuess: String): Guess
    fun clear()
}

class GuessSaveFailedLocalDataException(message: String?): Exception(message)
class GuessClearFailedLocalDataException(message: String?): Exception(message)