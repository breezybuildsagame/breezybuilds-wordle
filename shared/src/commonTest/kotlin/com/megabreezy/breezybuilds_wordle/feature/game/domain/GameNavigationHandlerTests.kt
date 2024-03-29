package com.megabreezy.breezybuilds_wordle.feature.game.domain

import com.megabreezy.breezybuilds_wordle.core.navigation.AppNavigationHandleable
import com.megabreezy.breezybuilds_wordle.core.navigation.AppRoute
import com.megabreezy.breezybuilds_wordle.core.navigation.NavigationDirection
import com.megabreezy.breezybuilds_wordle.core.navigation.SceneNavigationHandleable
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GameNavigationHandlerTests
{
    lateinit var appNavigator: MockAppNavigator

    @BeforeTest
    fun setUp()
    {
        appNavigator = MockAppNavigator()

        startKoin()
        {
            modules(
                GameKoinModule().module(),
                module { single<AppNavigationHandleable> { appNavigator } }
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `When the onSettingsOptionClicked method is invoked - the AppNavigationHandler navigate method is invoked - passing in expected route and direction parameters`()
    {
        // given
        val expectedAppRoute = AppRoute.SETTINGS
        val expectedDirection = NavigationDirection.INSTANT
        val sut = GameNavigationHandler()

        // when
        sut.onSettingsOptionClicked()

        // then
        assertEquals(expectedAppRoute, appNavigator.navigatedRoute)
        assertEquals(expectedDirection, appNavigator.navigatedDirection)
    }

    @Test
    fun `When the onGameOver method is invoked - the AppNavigationHandler navigate method is invoked - passing in expected route and direction parameters`()
    {
        // given
        val expectedAppRoute = AppRoute.STATS_GAME_OVER
        val expectedDirection = NavigationDirection.INSTANT
        val sut = GameNavigationHandler()

        // when
        sut.onGameOver()

        // then
        assertEquals(expectedAppRoute, appNavigator.navigatedRoute)
        assertEquals(expectedDirection, appNavigator.navigatedDirection)
    }

    @Test
    fun `When the onStatsOptionClicked method is invoked - the AppNavigationHandler navigate method is invoked - passing in expected route and direction parameters`()
    {
        // given
        val expectedAppRoute = AppRoute.STATS_GAME_IN_PROGRESS
        val expectedDirection = NavigationDirection.INSTANT
        val sut = GameNavigationHandler()

        // when
        sut.onStatsOptionClicked()

        // then
        assertEquals(expectedAppRoute, appNavigator.navigatedRoute)
        assertEquals(expectedDirection, appNavigator.navigatedDirection)
    }

    @Test
    fun `When the onHelpOptionClicked method is invoked - the AppNavigationHandler navigate method is invoked - passing in expected route and direction parameters`()
    {
        // given
        val expectedAppRoute = AppRoute.HELP
        val expectedDirection = NavigationDirection.INSTANT
        val sut = GameNavigationHandler()

        // when
        sut.onHelpOptionClicked()

        // then
        assertEquals(expectedAppRoute, appNavigator.navigatedRoute)
        assertEquals(expectedDirection, appNavigator.navigatedDirection)
    }

    class MockAppNavigator: AppNavigationHandleable
    {
        var navigatedRoute: AppRoute? = null
        var navigatedDirection: NavigationDirection? = null

        override fun navigate(route: AppRoute, direction: NavigationDirection)
        {
            navigatedRoute = route
            navigatedDirection = direction
        }

        override fun currentRoute(): AppRoute? = null
        override fun popBack(numberOfScreens: Int) { }
        override fun setSceneNavigator(newSceneNavigator: SceneNavigationHandleable) { }
    }
}