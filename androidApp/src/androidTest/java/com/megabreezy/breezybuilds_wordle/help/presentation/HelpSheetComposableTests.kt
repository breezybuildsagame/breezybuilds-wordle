package com.megabreezy.breezybuilds_wordle.help.presentation

import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.unit.Dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.help.presentation.HelpSheetComposable
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HelpSheetComposableTests
{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_when_tile_appears__view_matches_design_requirements()
    {
        // given
        var expectedTileSize: Dp? = null

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                expectedTileSize = LocalSceneDimensions.current.height.times(50 / Scene.idealFrame().height)

                HelpSheetComposable.Tile.Component()
            }
        }
        val displayedTile = composeTestRule.onNodeWithContentDescription(
            "${HelpSheetComposable.TagName.TILE}", useUnmergedTree = true)

        // then
        displayedTile.assertExists()
        displayedTile.assertWidthIsEqualTo(expectedTileSize!!)
        displayedTile.assertHeightIsEqualTo(expectedTileSize!!)
    }
}