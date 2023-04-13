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
    val gameBoardIsFull = gameBoard.rows().flatten().none { it.state() == GameBoard.Tile.State.HIDDEN } &&
            !gameBoard.rows().flatten().none { it.state() != GameBoard.Tile.State.HIDDEN }
    val gameBoardContainsCorrectAnswer = gameBoard.rows().firstOrNull ()
        { row -> row.all { it.state() == GameBoard.Tile.State.CORRECT } } != null

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

            finalizeActiveGameBoardRow()

            updateGameKeyboardHints()

            gameBoard.setNewActiveRow()
        }
    }

    if (resetIfNecessary && (!gameInProgress || gameBoardIsFull || gameBoardContainsCorrectAnswer)) gameBoard.reset()

    return gameBoard
}