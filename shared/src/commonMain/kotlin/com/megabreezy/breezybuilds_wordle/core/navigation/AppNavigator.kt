package com.megabreezy.breezybuilds_wordle.core.navigation

import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalRepresentable
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.StatsModal
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.use_case.StatsUseCase
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.use_case.getStatsModal
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppNavigator(
    private val modalAnimationDuration: Long = 300L
): AppNavigationHandleable, KoinComponent
{
    private val appModal: AppModalRepresentable by inject()
    private var routes = mutableListOf<AppRoute>()
    private var sceneNavigator: SceneNavigationHandleable? = null

    fun routes() = this.routes

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
                val modalContent = StatsUseCase().getStatsModal()
                modalContent.setPlayAgainButton(
                    newPlayAgainButton = StatsModal.Button(label = "Play Again")
                    {
                        appModal.handler()?.onModalShouldHide(animationDuration = this.modalAnimationDuration)
                        delay(this.modalAnimationDuration)
                        routes().clear()
                        navigate(route = AppRoute.GAME)
                    }
                )
                appModal.setContent(newContent = modalContent)
                appModal.handler()?.onModalShouldShow(animationDuration = this.modalAnimationDuration)
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