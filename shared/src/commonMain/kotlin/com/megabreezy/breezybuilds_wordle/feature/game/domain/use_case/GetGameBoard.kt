package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import org.koin.core.component.inject

fun GameUseCase.getGameBoard(): GameBoard
{
    val gameBoard: GameBoard by inject()

    return gameBoard
}