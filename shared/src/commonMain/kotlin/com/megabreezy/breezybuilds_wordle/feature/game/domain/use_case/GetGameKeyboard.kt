package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard
import org.koin.core.component.inject

fun GameUseCase.getGameKeyboard(): GameKeyboard
{
    val keyboard: GameKeyboard by inject()

    return keyboard
}