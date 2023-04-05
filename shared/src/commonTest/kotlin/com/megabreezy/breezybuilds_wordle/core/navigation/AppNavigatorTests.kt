package com.megabreezy.breezybuilds_wordle.core.navigation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppNavigatorTests
{
    @Test
    fun `when navigate invoked with AppRoute and NavigationDirection - sceneNavigator navigate method is invoked with expected parameters`()
    {
        // given
        val sut = AppNavigator()
        val sceneNavigator = MockSceneNavigator()
        sut.setSceneNavigator(newSceneNavigator = sceneNavigator)

        // when
        sut.navigate(route = AppRoute.GAME, NavigationDirection.BACKWARD)

        // then
        assertEquals(AppRoute.GAME, sceneNavigator.navigatedRoute)
        assertEquals(NavigationDirection.BACKWARD, sceneNavigator.navigatedDirection)
    }

    @Test
    fun `when navigate invoked with AppRoute - currentRout method returns expected AppRoute`()
    {
        // given
        val sut = AppNavigator()
        sut.navigate(route = AppRoute.SETTINGS)

        // when
        sut.navigate(route = AppRoute.GAME)

        // then
        assertEquals(AppRoute.GAME, sut.currentRoute())
    }

    @Test
    fun `When navigate invoked with Stats AppRoute - AppRoute does not change`()
    {
        // given
        val sut = AppNavigator()
        sut.navigate(route = AppRoute.GAME)

        // when
        sut.navigate(route = AppRoute.STATS)

        // then
        assertEquals(AppRoute.GAME, sut.currentRoute())
    }

    @Test
    fun `When navigate invoked with Stats AppRoute - the injected AppModalRepresentable setContent method is invoked passing in GetStatsModal use case`()
    {
        // given

        // when

        // then
        assertTrue(false)
    }

    @Test
    fun `When navigate invoked with Stats AppRoute - the AppModalViewHandler onModalShouldShow is invoked with expected animation duration`()
    {
        // given

        // when

        // then
        assertTrue(false)
    }

    @Test
    fun `When the StatsModal playAgain button is clicked - the AppModalViewHandler onModalShouldHide method is invoked`()
    {
        // given

        // when

        // then
        assertTrue(false)
    }

    @Test
    fun `When the StatsModal playAgain button is clicked - the current route is set to GAME and the sceneNavigator's navigate method is invoked`()
    {
        // given

        // when

        // then
        assertTrue(false)
    }

    class MockSceneNavigator(): SceneNavigationHandleable
    {
        var navigatedRoute: AppRoute? = null
        var navigatedDirection: NavigationDirection? = null

        override fun navigate(route: AppRoute, direction: NavigationDirection)
        {
            navigatedRoute = route
            navigatedDirection = direction
        }
    }
}