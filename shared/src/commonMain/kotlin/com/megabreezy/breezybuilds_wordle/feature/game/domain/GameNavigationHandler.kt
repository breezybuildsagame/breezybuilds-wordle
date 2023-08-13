package com.megabreezy.breezybuilds_wordle.feature.game.domain

import com.megabreezy.breezybuilds_wordle.core.navigation.AppNavigationHandleable
import com.megabreezy.breezybuilds_wordle.core.navigation.AppRoute
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameNavigationHandler: GameNavigationHandleable, KoinComponent
{
    val appNavigator: AppNavigationHandleable by inject()

    override fun onHelpOptionClicked()
    {
        appNavigator.navigate(route = AppRoute.HELP)
    }

    override fun onStatsOptionClicked()
    {
        appNavigator.navigate(route = AppRoute.STATS_GAME_IN_PROGRESS)
    }

    override fun onSettingsOptionClicked()
    {
        appNavigator.navigate(route = AppRoute.SETTINGS)
    }

    override fun onGameOver()
    {
        appNavigator.navigate(route = AppRoute.STATS_GAME_OVER)
    }
}