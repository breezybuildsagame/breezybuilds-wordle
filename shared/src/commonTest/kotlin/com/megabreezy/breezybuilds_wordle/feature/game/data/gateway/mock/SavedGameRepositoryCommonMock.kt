package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.mock

import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.SavedGameCreateFailedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.SavedGameGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.SavedGame

class SavedGameRepositoryCommonMock: SavedGameGateway
{
    var savedGameToReturn: SavedGame? = null

    var createShouldFail = false
    var createFailExceptionMessage = "Failed to create new SavedGame."

    override suspend fun create(): SavedGame
    {
        if (createShouldFail) throw SavedGameCreateFailedRepositoryException(createFailExceptionMessage)

        savedGameToReturn = savedGameToReturn ?: SavedGame()

        return savedGameToReturn!!
    }

}