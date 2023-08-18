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
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HelpSheetComposableTests
{
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockTiles: List<@Composable () -> Unit> = listOf(
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
                        tiles = expectedTiles
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
                    color = MaterialTheme.colorScheme.onSurface,
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

    @Test
    fun test_when_Instruction_initialized_with_String_parameter__expected_composable_is_displayed()
    {
        // given
        val expectedInstruction = "Guess the **WORDLE** in 6 tries."
        lateinit var expectedInstructionTextStyle: TextStyle

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                expectedInstructionTextStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = ThemeFonts.roboto,
                    fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(15 / Scene.idealFrame().height)),
                    fontWeight = FontWeight.Normal
                )

                HelpSheetComposable.Instruction.Component(
                    options = HelpSheetComposable.Instruction.ComponentOptions(
                        instruction = expectedInstruction
                    )
                )
            }
        }
        val displayedInstruction = composeTestRule.onNodeWithContentDescription(
            "${HelpSheetComposable.TagName.INSTRUCTION}", useUnmergedTree = true)

        // then
        displayedInstruction.onChildAt(index = 0).assertTextEquals(expectedInstruction)
        displayedInstruction.onChildAt(index = 1).assertContentDescriptionEquals("spacer")
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(HelpSheetComposable.Instruction.TextStyleKey, expectedInstructionTextStyle)
        ).assertExists()
    }

    @Test
    fun test_when_Composable_invoked__expected_content_container_is_displayed()
    {
        // given
        val expectedTagName = "${HelpSheetComposable.TagName.CONTENT}"

        // when
        composeTestRule.setContent()
        {
            SceneMock.display { HelpSheetComposable.Component() }
        }

        // then
        composeTestRule.onNodeWithContentDescription(expectedTagName).assertExists()
    }

    @Test
    fun test_when_Component_invoked_with_title_String_parameter__expected_title_is_displayed()
    {
        // given
        val expectedTitle = "TEST HOW TO PLAY"

        var expectedTitleWidth: Dp? = null

        lateinit var expectedTitleTextStyle: TextStyle

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                expectedTitleTextStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = ThemeFonts.roboto,
                    fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(20 / Scene.idealFrame().height)),
                    fontWeight = FontWeight.Black,
                    lineHeight = dpToSp(dp = LocalSceneDimensions.current.height.times(50 / Scene.idealFrame().height)),
                    textAlign = TextAlign.Center
                )
                expectedTitleWidth = LocalSceneDimensions.current.width.times(300 / Scene.idealFrame().width)

                HelpSheetComposable.Component(
                    options = HelpSheetComposable.ComponentOptions(
                        title = expectedTitle
                    )
                )
            }
        }
        val displayedContent = composeTestRule.onNodeWithContentDescription(
            "${HelpSheetComposable.TagName.CONTENT}", useUnmergedTree = true)

        // then
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(HelpSheetComposable.TitleTextStyleKey, expectedTitleTextStyle)
        ).assertExists()
        displayedContent.onChildAt(index = 0).assertTextEquals(expectedTitle)
        displayedContent.onChildAt(index = 0).assertWidthIsEqualTo(expectedTitleWidth!!)
    }

    @Test
    fun test_when_Component_invoked_with_Instruction_list__expected_instructions_are_displayed()
    {
        // given
        val instructionsList = listOf("Instruction 1", "Instruction 2")
        val expectedInstructions: List<@Composable () -> Unit> = instructionsList.map()
        {
            {
                HelpSheetComposable.Instruction.Component(
                    options = HelpSheetComposable.Instruction.ComponentOptions(
                        instruction = it
                    )
                )
            }
        }

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                HelpSheetComposable.Component(
                    options = HelpSheetComposable.ComponentOptions(
                        title = "HOW TO PLAY",
                        instructions = expectedInstructions
                    )
                )
            }
        }
        val displayedContent = composeTestRule.onNodeWithContentDescription(
            "${HelpSheetComposable.TagName.CONTENT}", useUnmergedTree = true)

        // then
        instructionsList.forEachIndexed()
        { index, text ->
            displayedContent.onChildAt(index = index + 1).assertContentDescriptionEquals("${HelpSheetComposable.TagName.INSTRUCTION}")
            displayedContent.onChildAt(index = index + 1).onChildAt(index = 0).assertTextEquals(text)
        }
    }

    @Test
    fun test_when_Component_invoked_with_Example_list__expected_examples_are_displayed()
    {
        // given
        val exampleText = listOf("Example 1", "Example 2")
        val exampleComposables: List<@Composable () -> Unit> = exampleText.map()
        {
            {
                HelpSheetComposable.Example.Component(
                    options = HelpSheetComposable.Example.ComponentOptions(
                        tiles = mockTiles,
                        description = it
                    )
                )
            }
        }

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                HelpSheetComposable.Component(
                    options = HelpSheetComposable.ComponentOptions(
                        title = "HOW TO PLAY",
                        examples = exampleComposables
                    )
                )
            }
        }
        val displayedContent = composeTestRule.onNodeWithContentDescription(
            "${HelpSheetComposable.TagName.CONTENT}", useUnmergedTree = true)

        // then
        exampleText.forEachIndexed()
        { index, text ->
            displayedContent.onChildAt(index = index + 1).assertContentDescriptionEquals("${HelpSheetComposable.TagName.EXAMPLE}")
            displayedContent.onChildAt(index = index + 1).onChildAt(index = 1).onChildAt(index = 0).assertTextEquals(text)
        }
    }

    @Test
    fun test_when_Component_invoked_with_footer_String_parameter__expected_text_is_displayed()
    {
        // given
        lateinit var expectedFooterTextStyle: TextStyle
        val expectedFooter = "No daily restrictions - play as often as you like!"
        val examples: List<@Composable () -> Unit> = listOf("Example 1", "Example 2").map()
        {
            {
                HelpSheetComposable.Example.Component(
                    options = HelpSheetComposable.Example.ComponentOptions(
                        tiles = mockTiles,
                        description = it
                    )
                )
            }
        }
        val instructions: List<@Composable () -> Unit> = listOf("Instruction 1", "Instruction 2").map()
        {
            {
                HelpSheetComposable.Instruction.Component(
                    options = HelpSheetComposable.Instruction.ComponentOptions(
                        instruction = it
                    )
                )
            }
        }

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                expectedFooterTextStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = ThemeFonts.roboto,
                    fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(15 / Scene.idealFrame().height)),
                    fontWeight = FontWeight.Black
                )

                HelpSheetComposable.Component(
                    options = HelpSheetComposable.ComponentOptions(
                        title = "HOW TO PLAY",
                        instructions = instructions,
                        examples = examples,
                        footer = expectedFooter
                    )
                )
            }
        }
        val displayedContent = composeTestRule.onNodeWithContentDescription(
            "${HelpSheetComposable.TagName.CONTENT}", useUnmergedTree = true)

        // then
        displayedContent.onChildAt(index = 5).assertTextEquals(expectedFooter)

        composeTestRule.onNode(
            SemanticsMatcher.expectValue(HelpSheetComposable.FooterTextStyleKey, expectedFooterTextStyle)
        ).assertExists()
    }

    @Test
    fun test_when_Component_invoked_with_CloseButton__expected_button_is_displayed()
    {
        // given
        val examples: List<@Composable () -> Unit> = listOf("Example 1", "Example 2").map()
        {
            {
                HelpSheetComposable.Example.Component(
                    options = HelpSheetComposable.Example.ComponentOptions(
                        tiles = mockTiles,
                        description = it
                    )
                )
            }
        }
        val instructions: List<@Composable () -> Unit> = listOf("Instruction 1", "Instruction 2").map()
        {
            {
                HelpSheetComposable.Instruction.Component(
                    options = HelpSheetComposable.Instruction.ComponentOptions(
                        instruction = it
                    )
                )
            }
        }
        var closeButtonClicked = false

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                HelpSheetComposable.Component(
                    options = HelpSheetComposable.ComponentOptions(
                        title = "HOW TO PLAY",
                        instructions = instructions,
                        examples = examples,
                        footer = "No daily restrictions - play as often as you like!",
                        closeButton = {
                            HelpSheetComposable.CloseButton.Component(
                                options = HelpSheetComposable.CloseButton.ComponentOptions { closeButtonClicked = true }
                            )
                        }
                    )
                )
            }
        }

        // then
        composeTestRule.onNodeWithContentDescription("${HelpSheetComposable.TagName.CLOSE_BUTTON}").assertExists()
        composeTestRule.onNodeWithContentDescription("${HelpSheetComposable.TagName.CLOSE_BUTTON}", useUnmergedTree = true)
            .onChildAt(index = 0).assertContentDescriptionEquals("spacer")
        composeTestRule.onNodeWithContentDescription("${HelpSheetComposable.TagName.CLOSE_BUTTON}", useUnmergedTree = true)
            .onChildAt(index = 1).performClick()
        composeTestRule.waitForIdle()
        Assert.assertTrue(closeButtonClicked)
    }
}