package com.megabreezy.breezybuilds_wordle.feature.game.presentation

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.AnnouncementRepresentable
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameHeader
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard

interface GameSceneViewModelRepresentable
{
    suspend fun setUp(gameSceneHandler: GameSceneHandleable? = null)

    fun getAnnouncement(): AnnouncementRepresentable

    fun getHeader(): GameHeader

    suspend fun getGameBoard(): GameBoard

    fun getGameKeyboard(): GameKeyboard
}