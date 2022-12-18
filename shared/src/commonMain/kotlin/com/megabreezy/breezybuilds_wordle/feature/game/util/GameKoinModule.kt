package com.megabreezy.breezybuilds_wordle.feature.game.util

import com.megabreezy.breezybuilds_wordle.feature.game.domain.GameNavigationHandleable
import com.megabreezy.breezybuilds_wordle.feature.game.domain.GameNavigationHandler
import org.koin.core.module.Module

class GameKoinModule
{
    fun module(): Module = org.koin.dsl.module()
    {
        single<GameNavigationHandleable> { GameNavigationHandler() }
    }
}