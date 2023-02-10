package com.megabreezy.breezybuilds_wordle.android.game.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene

object GameScene: Scene()
{
    @Composable
    override fun View()
    {
        var handler = rememberGameSceneHandler()

        LaunchedEffect(Unit) { handler.setUp() }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .semantics { contentDescription = TagName.CONTAINER.toString() },
            verticalArrangement = Arrangement.Top
        )
        {
            when (handler.activeView)
            {
                GameSceneHandler.ViewType.GAME -> {
                    handler.GameHeader()
                    RowSpacer(ratio = idealFrame().width / 63)
                    handler.GameBoard()
                    RowSpacer(modifier = Modifier.weight(1f))
                    handler.GameKeyboard()
                }
                else -> Unit
            }
        }
    }

    enum class TagName(private val id: String)
    {
        CONTAINER(id = "game_scene_container");

        override fun toString() = this.id
    }
}