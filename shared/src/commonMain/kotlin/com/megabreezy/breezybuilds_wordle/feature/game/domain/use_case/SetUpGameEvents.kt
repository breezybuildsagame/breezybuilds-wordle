package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard
import org.koin.core.component.inject

fun GameUseCase.setUpGameEvents()
{
    val keyboard: GameKeyboard by inject()

    keyboard.reset()
}