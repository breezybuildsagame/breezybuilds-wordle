package com.megabreezy.breezybuilds_wordle.android.game.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene

object GameScene: Scene()
{
    var sceneHandler: GameSceneHandler? = null

    @Composable
    override fun View()
    {
        val scope = rememberCoroutineScope()
        val handler = sceneHandler ?: rememberGameSceneHandler(scope = scope)

        LaunchedEffect(Unit) { handler.setUp() }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        )
        {
            when (handler.activeView)
            {
                GameSceneHandler.ViewType.GAME -> {
                    Box(modifier = Modifier.fillMaxSize())
                    {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .semantics { contentDescription = TagName.CONTAINER.toString() }
                        )
                        {
                            handler.GameHeader()
                            RowSpacer(ratio = idealFrame().width / 63)
                            handler.GameBoard()
                            RowSpacer(modifier = Modifier.weight(1f))
                            handler.GameKeyboard()
                        }
                    }
                    handler.gameAnnouncementText?.let()
                    {
                        Box(modifier = Modifier.fillMaxSize())
                        {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .semantics { contentDescription = TagName.OVERLAY.toString() }
                            )
                            {
                                RowSpacer(ratio = idealFrame().width / 200)
                                handler.GameAnnouncement()
                            }
                        }
                    }
                }
                else -> Unit
            }
        }
    }

    enum class TagName(private val id: String)
    {
        CONTAINER(id = "game_scene_container"), OVERLAY(id = "game_scene_overlay");

        override fun toString() = this.id
    }
}