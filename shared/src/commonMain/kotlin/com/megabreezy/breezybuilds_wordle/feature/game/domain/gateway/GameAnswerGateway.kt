package com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer

interface GameAnswerGateway
{
    suspend fun create(): GameAnswer
    fun get(): GameAnswer
    suspend fun updateAnswerGuessed(existingAnswer: GameAnswer): GameAnswer
    suspend fun updateAnswerNotGuessed(existingAnswer: GameAnswer): GameAnswer
}

class GameAnswerNotFoundRepositoryException(message: String? = null): Exception(message)
class GameAnswerNotCreatedRepositoryException(message: String? = null): Exception(message)
class GameAnswerNotUpdatedRepositoryException(message: String? = null): Exception(message)