package com.megabreezy.breezybuilds_wordle.core.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

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