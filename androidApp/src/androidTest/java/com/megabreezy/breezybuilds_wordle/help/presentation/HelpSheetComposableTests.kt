package com.megabreezy.breezybuilds_wordle.help.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.ui.tile.CoreTile
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.help.presentation.HelpSheetComposable
import com.megabreezy.breezybuilds_wordle.android.util.theme.ThemeFonts
import com.megabreezy.breezybuilds_wordle.android.util.theme.dpToSp
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import com.megabreezy.breezybuilds_wordle.feature.help.domain.model.HelpSheet
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HelpSheetComposableTests
{
    @get:Rule
    val composeTestRule = createComposeRule()

    val mockTiles: List<@Composable () -> Unit> = listOf(
        { HelpSheetComposable.Tile.Component(options = HelpSheetComposable.Tile.ComponentOptions(letter = "T", state = HelpSheet.Tile.State.CORRECT)) },
        { HelpSheetComposable.Tile.Component(options = HelpSheetComposable.Tile.ComponentOptions(letter = "E", state = HelpSheet.Tile.State.HIDDEN)) },
        { HelpSheetComposable.Tile.Component(options = HelpSheetComposable.Tile.ComponentOptions(letter = "S", state = HelpSheet.Tile.State.HIDDEN)) },
        { HelpSheetComposable.Tile.Component(options = HelpSheetComposable.Tile.ComponentOptions(letter = "T", state = HelpSheet.Tile.State.HIDDEN)) },
        { HelpSheetComposable.Tile.Component(options = HelpSheetComposable.Tile.ComponentOptions(letter = "S", state = HelpSheet.Tile.State.HIDDEN)) },
    )

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

    @Test
    fun test_when_hidden_tile_appears__view_matches_design_requirements()
    {
        // given
        var expectedBackgroundColor: Color? = null

        // when
        composeTestRule.setContent()
        {
            expectedBackgroundColor = getTileBackgroundColor(state = HelpSheet.Tile.State.HIDDEN)

            SceneMock.display()
            {
                HelpSheetComposable.Tile.Component(
                    options = HelpSheetComposable.Tile.ComponentOptions(
                        state = HelpSheet.Tile.State.HIDDEN
                    )
                )
            }
        }

        // then
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(CoreTile.BackgroundColorKey, expectedBackgroundColor!!)
        ).assertExists()
    }

    @Test
    fun test_when_tile_with_letter_appears__view_matches_design_requirements()
    {
        // given
        var expectedBackgroundColor: Color? = null

        // when
        composeTestRule.setContent()
        {
            expectedBackgroundColor = getTileBackgroundColor(state = HelpSheet.Tile.State.HIDDEN)

            SceneMock.display()
            {
                HelpSheetComposable.Tile.Component(
                    options = HelpSheetComposable.Tile.ComponentOptions(
                        letter = "G"
                    )
                )
            }
        }

        // then
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(CoreTile.BackgroundColorKey, expectedBackgroundColor!!)
        ).assertExists()
        composeTestRule.onNodeWithText("G").assertIsDisplayed()
    }

    @Test
    fun test_when_incorrect_tile_appears__view_matches_design_requirements()
    {
        // given
        var expectedBackgroundColor: Color? = null

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                expectedBackgroundColor = getTileBackgroundColor(state = HelpSheet.Tile.State.INCORRECT)

                HelpSheetComposable.Tile.Component(
                    options = HelpSheetComposable.Tile.ComponentOptions(
                        state = HelpSheet.Tile.State.INCORRECT
                    )
                )
            }
        }

        // then
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(CoreTile.BackgroundColorKey, expectedBackgroundColor!!)
        ).assertExists()
    }

    @Test
    fun test_when_correct_tile_appears__view_matches_design_requirements()
    {
        // given
        var expectedBackgroundColor: Color? = null

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                expectedBackgroundColor = getTileBackgroundColor(state = HelpSheet.Tile.State.CORRECT)

                HelpSheetComposable.Tile.Component(
                    options = HelpSheetComposable.Tile.ComponentOptions(
                        state = HelpSheet.Tile.State.CORRECT
                    )
                )
            }
        }

        // then
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(CoreTile.BackgroundColorKey, expectedBackgroundColor!!)
        ).assertExists()
    }

    @Composable
    private fun getTileBackgroundColor(state: HelpSheet.Tile.State): Color
    {
        return when(state)
        {
            HelpSheet.Tile.State.CLOSE -> MaterialTheme.colorScheme.tertiary
            HelpSheet.Tile.State.CORRECT -> MaterialTheme.colorScheme.secondary
            HelpSheet.Tile.State.INCORRECT -> MaterialTheme.colorScheme.error
            else -> Color.Transparent
        }
    }

    @Test
    fun test_when_Example_initialized_with_tiles__expected_Row_is_displayed()
    {
        // given
        val expectedTiles = mockTiles

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                HelpSheetComposable.Example.Component(
                    options = HelpSheetComposable.Example.ComponentOptions(
                        tiles = mockTiles
                    )
                )
            }
        }
        val displayedExample = composeTestRule.onNodeWithContentDescription(
            "${HelpSheetComposable.TagName.EXAMPLE}", useUnmergedTree = true)

        // then
        displayedExample.assertExists()

        "TESTS".forEachIndexed()
        { index, character ->
            displayedExample.onChildAt(index = 0).onChildAt(index = index).assertContentDescriptionEquals("${HelpSheetComposable.TagName.TILE}")
            displayedExample.onChildAt(index = 0).onChildAt(index = index).onChild().assertTextEquals("$character")
        }
    }

    @Test
    fun test_when_Example_initialized_with_description__expected_description_is_displayed()
    {
        // given
        val expectedDescriptionText = "Testing out **W** description line."
        lateinit var expectedDescriptionTextStyle: TextStyle

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                expectedDescriptionTextStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = ThemeFonts.roboto,
                    fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(15 / Scene.idealFrame().height)),
                    fontWeight = FontWeight.Normal
                )

                HelpSheetComposable.Example.Component(
                    options = HelpSheetComposable.Example.ComponentOptions(
                        tiles = mockTiles,
                        description = expectedDescriptionText
                    )
                )
            }
        }
        val displayedExample = composeTestRule.onNodeWithContentDescription(
            "${HelpSheetComposable.TagName.EXAMPLE}", useUnmergedTree = true)

        // then
        displayedExample.onChildAt(index = 1).onChildAt(index = 0).assertTextEquals(expectedDescriptionText)
        displayedExample.onChildAt(index = 1).onChildAt(index = 1).assertContentDescriptionEquals("spacer")
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(HelpSheetComposable.Example.DescriptionTextStyleKey, expectedDescriptionTextStyle)
        ).assertExists()
    }
}