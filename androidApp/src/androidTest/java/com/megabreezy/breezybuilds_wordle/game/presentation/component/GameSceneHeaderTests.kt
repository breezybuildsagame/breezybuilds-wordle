package com.megabreezy.breezybuilds_wordle.game.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.unit.Dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneHeader
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameSceneHeaderTests
{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_displaying_component__expected_tag_name_is_applied()
    {
        // given
        val expectedTagName = "game_scene_header_component"

        // when
        composeTestRule.setContent { GameSceneHeader.Component() }

        // then
        composeTestRule.onNodeWithContentDescription(expectedTagName).assertExists()
    }

    @Test
    fun when_displaying_component__frame_size_matches_design_requirements()
    {
        // given
        var expectedFrameWidth: Dp? = null
        var expectedFrameHeight: Dp? = null

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                BoxWithConstraints()
                {
                    expectedFrameWidth = this.maxWidth * (390 / Scene.idealFrame().width)
                    expectedFrameHeight = this.maxHeight * (100 / Scene.idealFrame().height)
                }

                GameSceneHeader.Component()
            }
        }

        // then
        composeTestRule.onNodeWithContentDescription(
            GameSceneHeader.TagName.COMPONENT.toString()
        ).assertWidthIsEqualTo(expectedFrameWidth!!)
        composeTestRule.onNodeWithContentDescription(
            GameSceneHeader.TagName.COMPONENT.toString()
        ).assertHeightIsEqualTo(expectedFrameHeight!!)
    }
}