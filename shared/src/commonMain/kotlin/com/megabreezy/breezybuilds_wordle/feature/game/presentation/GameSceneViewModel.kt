package com.megabreezy.breezybuilds_wordle.feature.game.presentation

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.*
import com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case.*

class GameSceneViewModel: GameSceneViewModelRepresentable
{
    override suspend fun setUp(gameSceneHandler: GameSceneHandleable?)
    {
        gameSceneHandler?.onStartingGame()
        GameUseCase().setUpGameEvents(sceneHandler = gameSceneHandler)
        println("game started")
        println("the answer for this game: ${GameUseCase().getGameAnswer()}")
        gameSceneHandler?.onGameStarted()
    }

    override fun getAnnouncement(): AnnouncementRepresentable = GameUseCase().getAnnouncement()

    override fun getHeader(): GameHeader = GameUseCase().getHeader()

    override suspend fun getGameBoard(): GameBoard = GameUseCase().getGameBoard()

    override fun getGameKeyboard(): GameKeyboard = GameUseCase().getGameKeyboard()
}