package com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer

interface GameAnswerGateway
{
    fun get(): GameAnswer
}

class GameAnswerNotFoundRepositoryException(message: String): Exception(message)