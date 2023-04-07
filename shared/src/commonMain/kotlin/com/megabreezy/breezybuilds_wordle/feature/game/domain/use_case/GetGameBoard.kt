package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import org.koin.core.component.inject

suspend fun GameUseCase.getGameBoard(
    resetIfNecessary: Boolean = false,
    reloadIfNecessary: Boolean = false
): GameBoard
{
    val gameBoard: GameBoard by inject()
    val gameInProgress: Boolean = try { getGameAnswer(attemptCreateOnFailure = false); true } catch(_: Throwable) { false }
    val answer = GameUseCase().getGameAnswer()

    if (gameBoard.rows().count() < 6)
    {
        val rows = mutableListOf<List<GameBoard.Tile>>()
        val tiles = mutableListOf<GameBoard.Tile>()

        (1 .. 6).forEach()
        { _ ->
            tiles.clear()
            (1 .. answer.word().length).forEach { _ -> tiles.add(GameBoard.Tile()) }
            rows.add(tiles.toList())
        }

        gameBoard.setRows(newRows = rows.toList())
    }

    if (gameInProgress && reloadIfNecessary)
    {
        val guessRepository: GameGuessGateway by inject()

        for (guess in try { guessRepository.getAll() } catch(_: Throwable) { listOf() } )
        {
            gameBoard.activeRow()?.forEachIndexed { index, tile -> tile.setLetter(newLetter = guess.word()[index]) }

            for (row in getGameKeyboard().rows())
            {
                for (key in row)
                {
                    gameBoard.activeRow()?.forEachIndexed()
                    { index, tile ->
                        tile.setLetter(newLetter = guess.word()[index])
                        if (tile.letter() == answer.word()[index])
                            tile.setState(newState = GameBoard.Tile.State.CORRECT)
                        else if (answer.word().contains("${tile.letter()}"))
                            tile.setState(newState = GameBoard.Tile.State.CLOSE)
                        else tile.setState(newState = GameBoard.Tile.State.INCORRECT)
                    }
                }
            }

            gameBoard.setNewActiveRow()
        }
    }

    if (resetIfNecessary && !gameInProgress) gameBoard.reset()

    return gameBoard
}