package com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameGuess

interface GameGuessGateway
{
    fun create(): GameGuess
    fun getAll(): List<GameGuess>
}

class GameGuessCreateFailedRepositoryException(message: String): Exception(message)