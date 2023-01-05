package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

fun GameUseCase.setUpGameEvents()
{
    val gameBoard = getGameBoard()
    val keyboard = getGameKeyboard()

    keyboard.reset()
    gameBoard.reset()
}