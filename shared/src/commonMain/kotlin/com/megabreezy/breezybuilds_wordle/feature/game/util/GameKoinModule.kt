package com.megabreezy.breezybuilds_wordle.feature.game.util

import com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.GameAnswerRepository
import com.megabreezy.breezybuilds_wordle.feature.game.domain.GameNavigationHandleable
import com.megabreezy.breezybuilds_wordle.feature.game.domain.GameNavigationHandler
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import org.koin.core.module.Module

class GameKoinModule
{
    fun module(): Module = org.koin.dsl.module()
    {
        single<GameNavigationHandleable> { GameNavigationHandler() }

        single<GameAnswerGateway> { GameAnswerRepository() }
    }
}