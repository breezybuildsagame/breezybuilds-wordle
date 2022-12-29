package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway

import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameAnswerRepository: GameAnswerGateway, KoinComponent
{
    private val localDataSource: WordLocalDataManageable by inject()

    override fun get(): GameAnswer
    {
        try
        {
            val word = localDataSource.get()
            return GameAnswer(word = word.word())
        }
        catch(e: WordNotFoundLocalDataException)
        {
            throw GameAnswerNotFoundRepositoryException(message = e.message)
        }
    }
}