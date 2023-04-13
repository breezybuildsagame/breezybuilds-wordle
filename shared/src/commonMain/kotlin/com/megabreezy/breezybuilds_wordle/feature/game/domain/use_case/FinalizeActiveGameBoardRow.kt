package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard
import org.koin.core.component.inject

suspend fun GameUseCase.finalizeActiveGameBoardRow()
{
    val gameBoard: GameBoard by inject()

    var answer = getGameAnswer().word()
    val keysToCompare: (String) -> List<GameKeyboard.Key> = { word ->
        getGameKeyboard().rows().flatten().filter { word.contains("${it.letter() ?: ""}") }
    }
    val newTileState: (Char, String, Int) -> GameBoard.Tile.State = {
        tileLetter, word, index ->
            if (tileLetter == word[index]) GameBoard.Tile.State.CORRECT
            else if (word.contains("$tileLetter")) GameBoard.Tile.State.CLOSE
            else GameBoard.Tile.State.INCORRECT
    }

    for (key in keysToCompare(answer))
    {
        gameBoard.activeRow()?.forEachIndexed()
        { index, tile ->
            if (tile.state() != GameBoard.Tile.State.HIDDEN) return

            tile.letter()?.let()
            { tileLetter ->
                tile.setState(newState = newTileState(tileLetter, answer, index))

                answer = answer.replaceFirst(oldValue = "$tileLetter", newValue = "_")
            }
        }
    }
}