package com.megabreezy.breezybuilds_wordle.core.data.source.guess

import com.megabreezy.breezybuilds_wordle.core.domain.model.Guess

interface GuessLocalDataManageable
{
    fun getAll(): List<Guess>
    suspend fun create(newGuess: String): Guess
    suspend fun clear()
}

class GuessSaveFailedLocalDataException(message: String?): Exception(message)
class GuessClearFailedLocalDataException(message: String?): Exception(message)