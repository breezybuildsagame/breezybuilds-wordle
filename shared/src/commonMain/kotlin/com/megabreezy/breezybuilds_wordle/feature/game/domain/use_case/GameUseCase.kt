package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import org.koin.core.component.KoinComponent

class GameUseCase: KoinComponent
{
    class GetGameAnswerFailedException(message: String): Exception(message)
    class GuessWordFailedException(message: String): Exception(message)
}