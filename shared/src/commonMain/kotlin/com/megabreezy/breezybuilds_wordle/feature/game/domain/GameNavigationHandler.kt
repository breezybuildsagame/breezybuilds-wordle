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
        TODO("Not yet implemented")
    }

    override fun onStatsOptionClicked()
    {
        TODO("Not yet implemented")
    }

    override fun onSettingsOptionClicked()
    {
        appNavigator.navigate(route = AppRoute.SETTINGS)
    }
}