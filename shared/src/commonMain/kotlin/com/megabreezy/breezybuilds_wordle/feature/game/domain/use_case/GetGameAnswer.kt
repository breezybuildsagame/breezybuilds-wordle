package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer
import org.koin.core.component.inject

fun GameUseCase.getGameAnswer(): GameAnswer
{
    val repository: GameAnswerGateway by inject()

    return try
    {
        repository.get()
    }
    catch (e: GameAnswerNotFoundRepositoryException)
    {
        throw GameUseCase.GetGameAnswerFailedException(message = e.message)
    }
}