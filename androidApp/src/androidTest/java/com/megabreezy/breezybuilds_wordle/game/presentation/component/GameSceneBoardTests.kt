package com.megabreezy.breezybuilds_wordle.game.presentation.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.unit.Dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneBoard
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameSceneBoardTests
{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_tile_composable_is_displayed__expected_tag_name_is_applied()
    {
        // given
        val expectedTagName = "game_scene_board_tile_component"

        // when
        composeTestRule.setContent { SceneMock.display { GameSceneBoard.Tile.Component() } }

        // then
        composeTestRule.onNodeWithContentDescription(expectedTagName).assertExists()
    }

    @Test
    fun when_tile_composable_is_displayed__component_frame_matches_design_requirements()
    {
        // given
        var expectedSize: Dp? = null

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                BoxWithConstraints()
                {
                    expectedSize = this.maxHeight * (61 / Scene.idealFrame().height)
                }

                GameSceneBoard.Tile.Component()
            }
        }

        // then
        composeTestRule.onNodeWithContentDescription(GameSceneBoard.TagName.TILE.toString()).assertWidthIsEqualTo(expectedSize!!)
        composeTestRule.onNodeWithContentDescription(GameSceneBoard.TagName.TILE.toString()).assertHeightIsEqualTo(expectedSize!!)
    }
}