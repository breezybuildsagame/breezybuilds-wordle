package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard

suspend fun GameUseCase.finalizeActiveGameBoardRow()
{
    for (key in getGameKeyboard().rows().flatten())
    {
        getGameBoard().activeRow()?.forEachIndexed()
        { index, tile ->
            if (tile.letter() == getGameAnswer().word()[index])
                tile.setState(newState = GameBoard.Tile.State.CORRECT)
            else if (getGameAnswer().word().contains("${tile.letter()}"))
                tile.setState(newState = GameBoard.Tile.State.CLOSE)
            else tile.setState(newState = GameBoard.Tile.State.INCORRECT)
        }
    }
}