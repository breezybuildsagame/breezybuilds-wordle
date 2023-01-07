package com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer

interface GameAnswerGateway
{
    fun create(): GameAnswer
    fun get(): GameAnswer
}

class GameAnswerNotFoundRepositoryException(message: String? = null): Exception(message)
class GameAnswerNotCreatedRepositoryException(message: String? = null): Exception(message)