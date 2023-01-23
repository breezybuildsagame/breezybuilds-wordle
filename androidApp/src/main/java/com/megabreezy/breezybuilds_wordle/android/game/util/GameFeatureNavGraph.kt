package com.megabreezy.breezybuilds_wordle.android.game.util

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.gameGraph()
{
    composable(route = "/game")
    {
        Box(
            contentAlignment = Alignment.Center
        )
        {
            Text(
                text = "Welcome to the game screen!",
                style = TextStyle(
                    color = Color.Green
                )
            )
        }
    }
}