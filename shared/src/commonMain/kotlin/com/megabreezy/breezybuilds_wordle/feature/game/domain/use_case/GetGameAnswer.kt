package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer
import org.koin.core.component.inject

suspend fun GameUseCase.getGameAnswer(attemptCreateOnFailure: Boolean = true): GameAnswer
{
    val repository: GameAnswerGateway by inject()
    var exception: Throwable?

    try { return repository.get() } catch (e: Throwable) { exception = e }

    if (attemptCreateOnFailure)
        try { return repository.create() } catch (e: Throwable) { exception = e }

    throw GameUseCase.GetGameAnswerFailedException(message = exception?.message)
}