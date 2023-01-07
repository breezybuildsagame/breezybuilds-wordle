package com.megabreezy.breezybuilds_wordle.core.navigation

interface SceneNavigationHandleable
{
    fun navigate(route: AppRoute, direction: NavigationDirection)
}