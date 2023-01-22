package com.megabreezy.breezybuilds_wordle.feature.game.util

import com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.GameAnswerRepository
import com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.GameGuessRepository
import com.megabreezy.breezybuilds_wordle.feature.game.domain.GameNavigationHandleable
import com.megabreezy.breezybuilds_wordle.feature.game.domain.GameNavigationHandler
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.Announcement
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.AnnouncementRepresentable
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard
import org.koin.core.module.Module

class GameKoinModule
{
    fun module(): Module = org.koin.dsl.module()
    {
        single { GameBoard() }

        single { GameKeyboard() }

        single<AnnouncementRepresentable> { Announcement() }

        single<GameNavigationHandleable> { GameNavigationHandler() }

        single<GameAnswerGateway> { GameAnswerRepository() }

        single<GameGuessGateway> { GameGuessRepository() }
    }
}