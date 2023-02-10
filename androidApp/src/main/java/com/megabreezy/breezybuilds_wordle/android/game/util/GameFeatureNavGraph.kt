package com.megabreezy.breezybuilds_wordle.android.game.util

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.megabreezy.breezybuilds_wordle.android.game.presentation.GameScene

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.gameGraph()
{
    composable(route = "/game") { GameScene.Stage() }
}