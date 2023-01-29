package com.megabreezy.breezybuilds_wordle.game.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
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
                    expectedBorder = BorderStroke(
                        color = MaterialTheme.colors.error,
                        width = this.maxHeight * (2 /Scene.idealFrame().height)
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
            SemanticsMatcher.expectValue(GameSceneBoard.Tile.BorderStrokeKey, expectedBorder)
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
                        color = MaterialTheme.colors.onPrimary,
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
            SemanticsMatcher.expectValue(GameSceneBoard.Tile.TextStyleKey, expectedTextStyle)
        ).assertExists()
        composeTestRule.onNodeWithContentDescription(GameSceneBoard.TagName.TILE.toString()).onChild().assertTextEquals("G")
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
            expectedBorder = BorderStroke(
                color = MaterialTheme.colors.error,
                width = 0.dp
            )
            expectedBackgroundColor = MaterialTheme.colors.error

            GameSceneBoard.Tile.Component(
                options = GameSceneBoard.Tile.ComponentOptions(
                    letter = "G",
                    state = GameBoard.Tile.State.INCORRECT
                )
            )
        }

        // then
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(GameSceneBoard.Tile.BackgroundColorKey, expectedBackgroundColor!!)
        ).assertExists()
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(GameSceneBoard.Tile.BorderStrokeKey, expectedBorder)
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
            expectedBorder = BorderStroke(
                color = MaterialTheme.colors.error,
                width = 0.dp
            )
            expectedBackgroundColor = MaterialTheme.colors.secondaryVariant

            GameSceneBoard.Tile.Component(
                options = GameSceneBoard.Tile.ComponentOptions(
                    letter = "G",
                    state = GameBoard.Tile.State.CLOSE
                )
            )
        }

        // then
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(GameSceneBoard.Tile.BackgroundColorKey, expectedBackgroundColor!!)
        ).assertExists()
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(GameSceneBoard.Tile.BorderStrokeKey, expectedBorder)
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
            expectedBorder = BorderStroke(
                color = MaterialTheme.colors.error,
                width = 0.dp
            )
            expectedBackgroundColor = MaterialTheme.colors.secondary

            GameSceneBoard.Tile.Component(
                options = GameSceneBoard.Tile.ComponentOptions(
                    letter = "G",
                    state = GameBoard.Tile.State.CORRECT
                )
            )
        }

        // then
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(GameSceneBoard.Tile.BackgroundColorKey, expectedBackgroundColor!!)
        ).assertExists()
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(GameSceneBoard.Tile.BorderStrokeKey, expectedBorder)
        ).assertExists()
    }
}