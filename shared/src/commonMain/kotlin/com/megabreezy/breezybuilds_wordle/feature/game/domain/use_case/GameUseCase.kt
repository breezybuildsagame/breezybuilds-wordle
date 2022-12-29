package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

class GameUseCase
{
    class GetGameAnswerFailedException(message: String): Exception(message)
    class GuessWordFailedException(message: String): Exception(message)
}