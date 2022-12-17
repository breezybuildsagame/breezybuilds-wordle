package com.megabreezy.breezybuilds_wordle.core.navigation

interface AppNavigationHandleable
{
    fun navigate(route: AppRoute, direction: NavigationDirection)
}

enum class NavigationDirection { FORWARD, BACKWARD, INSTANT }