package com.megabreezy.breezybuilds_wordle.core.navigation.mock

import com.megabreezy.breezybuilds_wordle.core.navigation.AppNavigationHandleable
import com.megabreezy.breezybuilds_wordle.core.navigation.AppRoute
import com.megabreezy.breezybuilds_wordle.core.navigation.NavigationDirection
import com.megabreezy.breezybuilds_wordle.core.navigation.SceneNavigationHandleable

class AppNavigatorCommonMock: AppNavigationHandleable
{
    var currentRouteToReturn: AppRoute? = null
    var navigatePassedInRoute: AppRoute? = null
    var navigatePassedInDirection: NavigationDirection? = null
    var popBackPassedInNumberOfScreens: Int? = null
    var currentSceneNavigator: SceneNavigationHandleable? = null

    override fun currentRoute(): AppRoute? = currentRouteToReturn

    override fun navigate(route: AppRoute, direction: NavigationDirection)
    {
        navigatePassedInRoute = route
        navigatePassedInDirection = direction
    }

    override fun popBack(numberOfScreens: Int)
    {
        popBackPassedInNumberOfScreens = numberOfScreens
    }

    override fun setSceneNavigator(newSceneNavigator: SceneNavigationHandleable)
    {
        this.currentSceneNavigator = newSceneNavigator
    }
}