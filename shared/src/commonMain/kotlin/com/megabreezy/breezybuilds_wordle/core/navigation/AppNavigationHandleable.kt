package com.megabreezy.breezybuilds_wordle.core.navigation

interface AppNavigationHandleable
{
    fun currentRoute(): AppRoute?
    fun navigate(route: AppRoute, direction: NavigationDirection = NavigationDirection.INSTANT)
    fun popBack(numberOfScreens: Int)
}

enum class NavigationDirection { FORWARD, BACKWARD, INSTANT }