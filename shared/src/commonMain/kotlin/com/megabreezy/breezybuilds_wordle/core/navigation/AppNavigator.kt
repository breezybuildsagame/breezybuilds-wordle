package com.megabreezy.breezybuilds_wordle.core.navigation

class AppNavigator: AppNavigationHandleable
{
    private var routes = mutableListOf<AppRoute>()
    private var sceneNavigator: SceneNavigationHandleable? = null

    fun setSceneNavigator(newSceneNavigator: SceneNavigationHandleable)
    {
        sceneNavigator = newSceneNavigator
    }

    override fun currentRoute(): AppRoute? = routes.lastOrNull()

    override fun navigate(route: AppRoute, direction: NavigationDirection)
    {
        routes.add(route)
        sceneNavigator?.navigate(route = route, direction = direction)
    }

    override fun popBack(numberOfScreens: Int)
    {
        TODO("Not yet implemented")
    }
}