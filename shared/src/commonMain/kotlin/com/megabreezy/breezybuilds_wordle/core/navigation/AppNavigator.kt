package com.megabreezy.breezybuilds_wordle.core.navigation

import com.megabreezy.breezybuilds_wordle.core.data.source.guess.GuessLocalDataManageable
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
    private val guessLocalDataSource: GuessLocalDataManageable by inject()
    private var routes = mutableListOf<AppRoute>()
    private var sceneNavigator: SceneNavigationHandleable? = null

    fun routes() = this.routes

    override fun setSceneNavigator(newSceneNavigator: SceneNavigationHandleable)
    {
        sceneNavigator = newSceneNavigator
    }

    override fun currentRoute(): AppRoute? = routes.lastOrNull()

    override fun navigate(route: AppRoute, direction: NavigationDirection)
    {
        when(route)
        {
            AppRoute.HELP ->
            {

            }
            AppRoute.STATS_GAME_IN_PROGRESS ->
            {
                val modalContent = StatsUseCase().getStatsModal()

                modalContent.setPlayAgainButton(
                    newPlayAgainButton = StatsModal.Button(label = "Continue Game")
                    {
                        appModal.handler()?.onModalShouldHide(animationDuration = this.modalAnimationDuration)
                    }
                )
                appModal.setContent(newContent = modalContent)
                appModal.handler()?.onModalShouldShow(animationDuration = this.modalAnimationDuration)
            }
            AppRoute.STATS_GAME_OVER ->
            {
                val modalContent = StatsUseCase().getStatsModal()

                if (guessLocalDataSource.getAll().isEmpty())
                {
                    modalContent.setPlayAgainButton(
                        newPlayAgainButton = StatsModal.Button(label = "Play Again")
                        {
                            appModal.handler()?.onModalShouldHide(animationDuration = this.modalAnimationDuration)
                            delay(this.modalAnimationDuration)
                            routes().clear()
                            navigate(route = AppRoute.GAME)
                        }
                    )
                }
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

class NavHelper: KoinComponent
{
    private val appNavigator: AppNavigationHandleable by inject()

    fun appNavigator() = this.appNavigator
}