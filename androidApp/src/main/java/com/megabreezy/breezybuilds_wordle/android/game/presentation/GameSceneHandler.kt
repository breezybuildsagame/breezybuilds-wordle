package com.megabreezy.breezybuilds_wordle.android.game.presentation

import androidx.compose.runtime.*
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneBoard
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneHeader
import com.megabreezy.breezybuilds_wordle.feature.game.presentation.GameSceneHandleable
import com.megabreezy.breezybuilds_wordle.feature.game.presentation.GameSceneViewModel

class GameSceneHandler: GameSceneHandleable
{
    private var viewModel = GameSceneViewModel()

    var activeView by mutableStateOf(ViewType.EMPTY)
    var gameBoardRows by mutableStateOf(listOf<@Composable () -> Unit>())
    var gameKeyboardIsEnabled by mutableStateOf(false)

    fun setUp()
    {
        viewModel.setUp(handler = this)
    }

    override fun onAnnouncementShouldShow()
    {
        TODO("Not yet implemented")
    }

    override fun onAnnouncementShouldHide()
    {
        TODO("Not yet implemented")
    }

    override fun onGameOver()
    {
        TODO("Not yet implemented")
    }

    override fun onGameStarted()
    {
        val updatedGameBoardRows = mutableListOf<@Composable () -> Unit>()
        val updatedGameBoardRowTiles = mutableListOf<@Composable () -> Unit>()

        viewModel.getGameBoard().rows().forEach()
        { middleRow ->

            updatedGameBoardRowTiles.clear()

            middleRow.forEach()
            { middleTile ->
                updatedGameBoardRowTiles.add {
                    GameSceneBoard.Tile.Component(
                        options = GameSceneBoard.Tile.ComponentOptions(
                            state = middleTile.state(),
                            letter = middleTile.letter()?.toString() ?: ""
                        )
                    )
                }
            }

            updatedGameBoardRows.add {
                GameSceneBoard.Row.Component(
                    options = GameSceneBoard.Row.ComponentOptions(tiles = updatedGameBoardRowTiles.toList())
                )
            }
        }

        gameBoardRows = updatedGameBoardRows

        activeView = ViewType.GAME
        gameKeyboardIsEnabled = true
    }

    override fun onGuessingWord()
    {
        TODO("Not yet implemented")
    }

    override fun onRevealNextTile()
    {
        TODO("Not yet implemented")
    }

    override fun onRoundCompleted()
    {
        TODO("Not yet implemented")
    }

    override fun onStartingGame()
    {
        activeView = ViewType.GAME
        gameKeyboardIsEnabled = false
    }

    @Composable
    fun GameHeader()
    {
        GameSceneHeader.Component(
            options = GameSceneHeader.ComponentOptions(
                text = viewModel.getHeader().title()
            )
        )
    }

    @Composable
    fun GameBoard()
    {
        GameSceneBoard.Component(options = GameSceneBoard.ComponentOptions(rows = gameBoardRows))
    }

    enum class ViewType { EMPTY, GAME }
}

@Composable
fun rememberGameSceneHandler() = remember { GameSceneHandler() }