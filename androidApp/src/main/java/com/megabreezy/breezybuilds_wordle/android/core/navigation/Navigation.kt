package com.megabreezy.breezybuilds_wordle.android.core.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.megabreezy.breezybuilds_wordle.android.game.util.gameGraph

@ExperimentalAnimationApi
@Composable
fun Navigation()
{
    val navController = rememberAnimatedNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = "/game"
    )
    {
        gameGraph()
    }
}