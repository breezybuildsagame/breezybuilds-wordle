package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway

import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameLocalDataManageable
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.SavedGameCreateFailedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.SavedGameGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.SavedGame
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SavedGameRepository: SavedGameGateway, KoinComponent
{
    private val completedGameLocalDataSource: CompletedGameLocalDataManageable by inject()
    private val gameAnswerRepository: GameAnswerGateway by inject()

    override suspend fun create(): SavedGame
    {
        try
        {
            val currentAnswer = gameAnswerRepository.get()

            return SavedGame()
        }
        catch(e: Throwable)
        {
            when (e)
            {
                is GameAnswerNotFoundRepositoryException ->
                    throw SavedGameCreateFailedRepositoryException(message = e.message)
                else -> throw e
            }
        }
    }
}