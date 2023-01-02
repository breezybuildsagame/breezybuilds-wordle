package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import org.koin.core.component.inject

fun GameUseCase.getGameBoard(): GameBoard
{
    val gameBoard: GameBoard by inject()

    if (gameBoard.rows().count() < 6)
    {
        val answer = GameUseCase().getGameAnswer()
        val rows = mutableListOf<List<GameBoard.Tile>>()
        val tiles = mutableListOf<GameBoard.Tile>()

        (1 .. answer.word().length).forEach { _ -> tiles.add(GameBoard.Tile()) }
        (1 .. 6).forEach { _ -> rows.add(tiles.toList()) }

        gameBoard.setRows(newRows = rows.toList())
    }

    return gameBoard
}