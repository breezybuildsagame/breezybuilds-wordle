package com.megabreezy.breezybuilds_wordle.core.navigation

import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalRepresentable
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.use_case.StatsUseCase
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.use_case.getStatsModal
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppNavigator: AppNavigationHandleable, KoinComponent
{
    private val appModal: AppModalRepresentable by inject()
    private var routes = mutableListOf<AppRoute>()
    private var sceneNavigator: SceneNavigationHandleable? = null

    fun setSceneNavigator(newSceneNavigator: SceneNavigationHandleable)
    {
        sceneNavigator = newSceneNavigator
    }

    override fun currentRoute(): AppRoute? = routes.lastOrNull()

    override fun navigate(route: AppRoute, direction: NavigationDirection)
    {
        when(route)
        {
            AppRoute.STATS ->
            {
                appModal.setContent(newContent = StatsUseCase().getStatsModal())
            }
            else -> {
                routes.add(route)
                sceneNavigator?.navigate(route = route, direction = direction)
            }
        }
    }

    override fun popBack(numberOfScreens: Int)
    {
        TODO("Not yet implemented")
    }
}