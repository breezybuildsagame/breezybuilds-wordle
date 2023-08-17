package com.megabreezy.breezybuilds_wordle.game.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.ui.tile.CoreTile
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneBoard
import com.megabreezy.breezybuilds_wordle.android.util.theme.ThemeFonts
import com.megabreezy.breezybuilds_wordle.android.util.theme.dpToSp
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
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
        val expectedTagName = "${CoreTile.TagName.TILE}"

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
        composeTestRule.onNodeWithContentDescription(CoreTile.TagName.TILE.toString()).assertWidthIsEqualTo(expectedSize!!)
        composeTestRule.onNodeWithContentDescription(CoreTile.TagName.TILE.toString()).assertHeightIsEqualTo(expectedSize!!)
    }

    @Test
    fun when_hidden_tile_is_displayed__component_matches_design_requirements()
    {
        // given
        lateinit var expectedBorder: BorderStroke

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                BoxWithConstraints()
                {
                    val expectedBorderWidth: Dp = LocalSceneDimensions.current.height *
                            (61f / Scene.idealFrame().height) *
                            (2f / 61f)
                    expectedBorder = BorderStroke(
                        color = MaterialTheme.colorScheme.error,
                        width = expectedBorderWidth
                    )
                }

                GameSceneBoard.Tile.Component(
                    options = GameSceneBoard.Tile.ComponentOptions(
                        state = GameBoard.Tile.State.HIDDEN
                    )
                )
            }
        }

        // then
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(CoreTile.BorderStrokeKey, expectedBorder)
        ).assertExists()
    }

    @Test
    fun when_tile_with_letter_is_displayed__component_matches_design_requirements()
    {
        // given
        lateinit var expectedTextStyle: TextStyle

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                BoxWithConstraints()
                {
                    expectedTextStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = ThemeFonts.roboto,
                        fontSize = dpToSp(dp = this.maxHeight * (35 / Scene.idealFrame().height)),
                        fontWeight = FontWeight.Bold
                    )
                }

                GameSceneBoard.Tile.Component(options = GameSceneBoard.Tile.ComponentOptions(letter = "G"))
            }
        }

        // then
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(CoreTile.TextStyleKey, expectedTextStyle)
        ).assertExists()
        composeTestRule.onNodeWithContentDescription(CoreTile.TagName.TILE.toString()).onChild().assertTextEquals("G")
    }

    @Test
    fun when_incorrect_tile_appears__view_matches_design_requirements()
    {
        // given
        lateinit var expectedBorder: BorderStroke
        var expectedBackgroundColor: Color? = null

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                expectedBorder = BorderStroke(
                    color = Color.Transparent,
                    width = 0.dp
                )
                expectedBackgroundColor = MaterialTheme.colorScheme.error

                GameSceneBoard.Tile.Component(
                    options = GameSceneBoard.Tile.ComponentOptions(
                        letter = "G",
                        state = GameBoard.Tile.State.INCORRECT
                    )
                )
            }
        }

        // then
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(CoreTile.BackgroundColorKey, expectedBackgroundColor!!)
        ).assertExists()
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(CoreTile.BorderStrokeKey, expectedBorder)
        ).assertExists()
    }

    @Test
    fun when_close_tile_appears__view_matches_design_requirements()
    {
        // given
        lateinit var expectedBorder: BorderStroke
        var expectedBackgroundColor: Color? = null

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                expectedBorder = BorderStroke(
                    color = Color.Transparent,
                    width = 0.dp
                )
                expectedBackgroundColor = MaterialTheme.colorScheme.tertiary

                GameSceneBoard.Tile.Component(
                    options = GameSceneBoard.Tile.ComponentOptions(
                        letter = "G",
                        state = GameBoard.Tile.State.CLOSE
                    )
                )
            }
        }

        // then
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(CoreTile.BackgroundColorKey, expectedBackgroundColor!!)
        ).assertExists()
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(CoreTile.BorderStrokeKey, expectedBorder)
        ).assertExists()
    }

    @Test
    fun when_correct_tile_appears__view_matches_design_requirements()
    {
        // given
        lateinit var expectedBorder: BorderStroke
        var expectedBackgroundColor: Color? = null

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                expectedBorder = BorderStroke(
                    color = Color.Transparent,
                    width = 0.dp
                )
                expectedBackgroundColor = MaterialTheme.colorScheme.secondary

                GameSceneBoard.Tile.Component(
                    options = GameSceneBoard.Tile.ComponentOptions(
                        letter = "G",
                        state = GameBoard.Tile.State.CORRECT
                    )
                )
            }
        }

        // then
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(CoreTile.BackgroundColorKey, expectedBackgroundColor!!)
        ).assertExists()
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(CoreTile.BorderStrokeKey, expectedBorder)
        ).assertExists()
    }

    @Test
    fun when_view_appears_with_Rows__view_matches_design_requirements()
    {
        // given
        var expectedRowSpacerHeight: Dp? = null
        var expectedTileSpacerWidth: Dp? = null
        val expectedRows: List<@Composable () -> Unit> = listOf(
            {
                GameSceneBoard.Row.Component(
                    options = GameSceneBoard.Row.ComponentOptions(
                        tiles = listOf(
                            { GameSceneBoard.Tile.Component(options = GameSceneBoard.Tile.ComponentOptions(letter = "A")) },
                            { GameSceneBoard.Tile.Component(options = GameSceneBoard.Tile.ComponentOptions(letter = "B")) },
                        )
                    )
                )
            },
            {
                GameSceneBoard.Row.Component(
                    options = GameSceneBoard.Row.ComponentOptions(
                        tiles = listOf(
                            { GameSceneBoard.Tile.Component(options = GameSceneBoard.Tile.ComponentOptions(letter = "C")) },
                            { GameSceneBoard.Tile.Component(options = GameSceneBoard.Tile.ComponentOptions(letter = "D")) },
                            { GameSceneBoard.Tile.Component(options = GameSceneBoard.Tile.ComponentOptions(letter = "E")) },
                        )
                    )
                )
            }
        )

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                BoxWithConstraints()
                {
                    expectedTileSpacerWidth = this.maxWidth * (7 / Scene.idealFrame().width)
                    expectedRowSpacerHeight = this.maxHeight * (10 / Scene.idealFrame().height)
                }

                GameSceneBoard.Component(options = GameSceneBoard.ComponentOptions(rows = expectedRows))
            }
        }
        val displayedBoard = composeTestRule.onNodeWithContentDescription(GameSceneBoard.TagName.BOARD.toString(), useUnmergedTree = true)

        // then
        displayedBoard.onChildAt(index = 0).onChildAt(index = 0).assertTextEquals("A")
        displayedBoard.onChildAt(index = 1).assertContentDescriptionEquals("spacer")
        displayedBoard.onChildAt(index = 1).assertWidthIsEqualTo(expectedTileSpacerWidth!!)
        displayedBoard.onChildAt(index = 2).onChildAt(index = 0).assertTextEquals("B")
        displayedBoard.onChildAt(index = 3).assertContentDescriptionEquals("spacer")
        displayedBoard.onChildAt(index = 3).assertHeightIsEqualTo(expectedRowSpacerHeight!!)

        displayedBoard.onChildAt(index = 4).onChildAt(index = 0).assertTextEquals("C")
        displayedBoard.onChildAt(index = 5).assertContentDescriptionEquals("spacer")
        displayedBoard.onChildAt(index = 5).assertWidthIsEqualTo(expectedTileSpacerWidth!!)
        displayedBoard.onChildAt(index = 6).onChildAt(index = 0).assertTextEquals("D")
        displayedBoard.onChildAt(index = 7).assertContentDescriptionEquals("spacer")
        displayedBoard.onChildAt(index = 7).assertWidthIsEqualTo(expectedTileSpacerWidth!!)
        displayedBoard.onChildAt(index = 8).onChildAt(index = 0).assertTextEquals("E")
        displayedBoard.onChildAt(index = 9).assertDoesNotExist()
    }
}