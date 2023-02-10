package com.megabreezy.breezybuilds_wordle.android.game.presentation

import androidx.compose.runtime.*
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneAnnouncement
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneBoard
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneHeader
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneKeyboard
import com.megabreezy.breezybuilds_wordle.feature.game.presentation.GameSceneHandleable
import com.megabreezy.breezybuilds_wordle.feature.game.presentation.GameSceneViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class GameSceneHandler(private val scope: CoroutineScope? = null): GameSceneHandleable
{
    private var viewModel = GameSceneViewModel()

    var activeView by mutableStateOf(ViewType.EMPTY)
    var gameAnnouncementText by mutableStateOf(viewModel.getAnnouncement().message())
    var middleGameBoardRows by mutableStateOf(viewModel.getGameBoard().rows())
    var gameKeyboardRows by mutableStateOf(listOf<List<@Composable () -> Unit>>())
    var gameKeyboardIsEnabled by mutableStateOf(false)

    fun setUp() { viewModel.setUp(handler = this) }

    private fun updateGameKeyboardRows()
    {
        val updatedGameKeyboardRows = mutableListOf<List<@Composable () -> Unit>>()

        viewModel.getGameKeyboard().rows().forEach()
        { middleRow ->
            val updatedGameKeyboardRowKeys = mutableListOf<@Composable () -> Unit>()

            middleRow.forEach()
            { middleKey ->
                updatedGameKeyboardRowKeys.add()
                {
                    GameSceneKeyboard.Key.Component(
                        options = GameSceneKeyboard.Key.ComponentOptions(
                            letters = middleKey.letters(),
                            resourceId = middleKey.resourceId(),
                            backgroundColor = middleKey.backgroundColor(),
                            onClick = { if (gameKeyboardIsEnabled) scope?.launch { middleKey.click() } }
                        )
                    )
                }
            }

            updatedGameKeyboardRows.add(updatedGameKeyboardRowKeys.toList())
        }

        gameKeyboardRows = updatedGameKeyboardRows.toList()
    }

    private fun updateMutableStates(keyboardIsEnabled: Boolean = false)
    {
        updateGameKeyboardRows()
        gameKeyboardIsEnabled = keyboardIsEnabled
        gameAnnouncementText = viewModel.getAnnouncement().message()
        middleGameBoardRows = viewModel.getGameBoard().rows()

        activeView = ViewType.GAME
    }

    override fun onAnnouncementShouldShow() { updateMutableStates(keyboardIsEnabled = true) }

    override fun onAnnouncementShouldHide() { updateMutableStates(keyboardIsEnabled = true) }

    override fun onGameOver() { updateMutableStates(keyboardIsEnabled = false) }

    override fun onGameStarted() { updateMutableStates(keyboardIsEnabled = true) }

    override fun onGuessingWord() { updateMutableStates() }

    override fun onRevealNextTile() { updateMutableStates(keyboardIsEnabled = true) }

    override fun onRoundCompleted() { updateMutableStates(keyboardIsEnabled = true) }

    override fun onStartingGame() { updateMutableStates() }

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
        val composableGameBoardRows = mutableListOf<@Composable () -> Unit>()
        val composableGameBoardRowTiles = mutableListOf<@Composable () -> Unit>()

        middleGameBoardRows.forEach()
        { middleRow ->

            composableGameBoardRowTiles.clear()

            middleRow.forEach()
            { middleTile ->
                composableGameBoardRowTiles.add {
                    GameSceneBoard.Tile.Component(
                        options = GameSceneBoard.Tile.ComponentOptions(
                            state = middleTile.state(),
                            letter = middleTile.letter()?.toString() ?: ""
                        )
                    )
                }
            }

            composableGameBoardRows.add {
                GameSceneBoard.Row.Component(
                    options = GameSceneBoard.Row.ComponentOptions(tiles = composableGameBoardRowTiles.toList())
                )
            }
        }

        GameSceneBoard.Component(options = GameSceneBoard.ComponentOptions(rows = composableGameBoardRows))
    }

    @Composable
    fun GameKeyboard()
    {
        GameSceneKeyboard.Component(options = GameSceneKeyboard.ComponentOptions(rows = gameKeyboardRows))
    }

    @Composable
    fun GameAnnouncement()
    {
        gameAnnouncementText?.let()
        {
            GameSceneAnnouncement.Component(options = GameSceneAnnouncement.ComponentOptions(text = it))
        }
    }

    enum class ViewType { EMPTY, GAME }
}

@Composable
fun rememberGameSceneHandler(scope: CoroutineScope? = null) = remember { GameSceneHandler(scope = scope) }