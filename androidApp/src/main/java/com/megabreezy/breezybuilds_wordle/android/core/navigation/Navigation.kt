package com.megabreezy.breezybuilds_wordle.android.core.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.megabreezy.breezybuilds_wordle.android.game.util.gameGraph
import com.megabreezy.breezybuilds_wordle.core.navigation.NavHelper

@ExperimentalAnimationApi
@Composable
fun Navigation()
{
    val navController = rememberAnimatedNavController()
    val scope = rememberCoroutineScope()

    val handler = remember()
    {
        SceneNavigationHandler(
            navController = navController,
            scope = scope
        )
    }

    LaunchedEffect(Unit)
    {
        NavHelper().appNavigator().setSceneNavigator(newSceneNavigator = handler)
    }

    AnimatedNavHost(
        navController = navController,
        startDestination = "/game"
    )
    {
        gameGraph()
    }
}