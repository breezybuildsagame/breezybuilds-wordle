package com.megabreezy.breezybuilds_wordle.feature.game.presentation

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.*
import com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case.*

class GameSceneViewModel
{
    suspend fun setUp(handler: GameSceneHandleable? = null)
    {
        handler?.onStartingGame()
        GameUseCase().setUpGameEvents(sceneHandler = handler)
        println("game started")
        println("the answer for this game: ${GameUseCase().getGameAnswer()}")
        handler?.onGameStarted()
    }

    fun getAnnouncement(): AnnouncementRepresentable = GameUseCase().getAnnouncement()

    fun getHeader(): GameHeader = GameUseCase().getHeader()

    suspend fun getGameBoard(): GameBoard = GameUseCase().getGameBoard()

    fun getGameKeyboard(): GameKeyboard = GameUseCase().getGameKeyboard()
}