package com.megabreezy.breezybuilds_wordle.android.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp

open class Scene
{
    companion object
    {
        fun idealFrame() = SizingFrame(width = 390f, height = 844f)
    }

    @Composable
    fun Stage()
    {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        )
        {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            )
            {
                View()
            }
        }
    }

    @Composable
    private fun SceneSpacer(
        modifier: Modifier = Modifier,
        ratio: Float? = null,
        type: String = "COLUMN",
    )
    {
        if (type == "COLUMN") modifier.fillMaxHeight() else modifier.fillMaxWidth()

        ratio?.let()
        {
            return Spacer(
                modifier = modifier
                    .aspectRatio(it)
                    .semantics { contentDescription = "spacer" }
            )
        }

        Spacer(modifier = modifier.semantics { contentDescription = "spacer" })
    }

    @Composable
    fun ColumnSpacer(
        modifier: Modifier = Modifier,
        ratio: Float? = null
    )
    {
        SceneSpacer(modifier = modifier, ratio = ratio)
    }

    @Composable
    fun RowSpacer(
        modifier: Modifier = Modifier,
        ratio: Float? = null
    )
    {
        SceneSpacer(modifier = modifier, ratio = ratio, type = "ROW")
    }

    @Composable
    open fun View()
    {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center)
        {
            Text(fontSize = 20.sp, text = "Override the Scene() method")
        }
    }
}

data class SizingFrame(val width: Float, val height: Float)
data class LayoutFrame(val width: Dp, val height: Dp)
