package com.megabreezy.breezybuilds_wordle.core.data.source.completed_game

import com.megabreezy.breezybuilds_wordle.core.domain.model.CompletedGame
import kotlin.coroutines.cancellation.CancellationException

interface CompletedGameLocalDataManageable
{
    @Throws(CompletedGameNotFoundLocalDataException::class)
    fun getAll(): List<CompletedGame>

    @Throws(
        CancellationException::class,
        CompletedGameNotSavedLocalDataException::class
    )
    suspend fun put(newCompletedGame: CompletedGame): CompletedGame
}

class CompletedGameNotFoundLocalDataException(message: String? = null): Exception(message)
class CompletedGameNotSavedLocalDataException(message: String? = null): Exception(message)