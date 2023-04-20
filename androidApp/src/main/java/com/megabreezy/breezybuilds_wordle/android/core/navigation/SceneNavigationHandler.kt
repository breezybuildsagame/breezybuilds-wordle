package com.megabreezy.breezybuilds_wordle.android.core.navigation

import androidx.navigation.NavController
import com.megabreezy.breezybuilds_wordle.core.navigation.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SceneNavigationHandler(
    private val appNavigator: AppNavigationHandleable = NavHelper().appNavigator(),
    private val navController: NavController? = null,
    private val scope: CoroutineScope? = null
): SceneNavigationHandleable
{
    fun setUp()
    {
        appNavigator.setSceneNavigator(newSceneNavigator = this)
    }

    override fun navigate(route: AppRoute, direction: NavigationDirection)
    {
        try
        {
            navController?.navigate(route.name) { popUpTo(0) }
            println("Navigating to $route")
        }
        catch (e: Throwable)
        {
            scope?.launch { appNavigator.popBack(numberOfScreens = 1) }
        }
    }
}