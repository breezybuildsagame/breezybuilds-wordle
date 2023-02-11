package com.megabreezy.breezybuilds_wordle.core.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.megabreezy.breezybuilds_wordle.android.MyApplicationTheme
import com.megabreezy.breezybuilds_wordle.android.core.ui.LayoutFrame
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions

class SceneMock(
    private val frame: LayoutFrame = LayoutFrame(
        width = idealFrame().width.dp,
        height = idealFrame().height.dp
    ),
    private val content: @Composable () -> Unit
): Scene()
{
    @Composable
    override fun View()
    {
        MyApplicationTheme()
        {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize().background(Color(0xFF202020)),
            )
            {
                CompositionLocalProvider(LocalSceneDimensions provides frame)
                {
                    content()
                }
            }
        }
    }

    companion object
    {
        @SuppressLint("ComposableNaming")
        @Composable
        fun display(content: @Composable () -> Unit)
        {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            )
            {
                BoxWithConstraints(
                    modifier = Modifier
                        .aspectRatio(ratio = idealFrame().width / idealFrame().height)
                        .fillMaxHeight()
                )
                {
                    SceneMock(frame = LayoutFrame(width = this.maxWidth, height = this.maxHeight))
                    {
                        content()
                    }.Stage()
                }
            }
        }
    }
}