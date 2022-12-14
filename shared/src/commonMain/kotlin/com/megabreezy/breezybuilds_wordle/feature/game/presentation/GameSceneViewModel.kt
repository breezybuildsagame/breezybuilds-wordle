package com.megabreezy.breezybuilds_wordle.feature.game.presentation

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.Announcement
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameHeader
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard
import com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case.*

class GameSceneViewModel
{
    fun setUp() { GameUseCase().setUpGameEvents() }

    fun getAnnouncement(): Announcement = GameUseCase().getAnnouncement()

    fun getHeader(): GameHeader = GameUseCase().getHeader()

    fun getGameBoard(): GameBoard = GameUseCase().getGameBoard()

    fun getGameKeyboard(): GameKeyboard = GameUseCase().getGameKeyboard()
}