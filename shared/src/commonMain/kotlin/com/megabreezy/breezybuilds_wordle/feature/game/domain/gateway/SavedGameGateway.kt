package com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.SavedGame

interface SavedGameGateway
{
    fun create(): SavedGame
}

class SavedGameCreateFailedRepositoryException(message: String?): Exception(message)