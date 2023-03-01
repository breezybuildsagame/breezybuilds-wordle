package com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameGuess

interface GameGuessGateway
{
    suspend fun create(): GameGuess
    fun getAll(): List<GameGuess>
    suspend fun clear()
}

class GameGuessCreateFailedRepositoryException(message: String?): Exception(message)
class GameGuessNotFoundRepositoryException(message: String?): Exception(message)