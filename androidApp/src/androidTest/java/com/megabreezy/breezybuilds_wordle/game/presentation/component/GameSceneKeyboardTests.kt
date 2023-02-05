package com.megabreezy.breezybuilds_wordle.game.presentation.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.Dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneKeyboard
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameSceneKeyboardTests
{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_key_composable_is_displayed__expected_tagName_is_applied()
    {
        // given
        val expectedTagName = "game_scene_keyboard_key_component"

        // when
        composeTestRule.setContent { SceneMock.display { GameSceneKeyboard.Key.Component() } }

        // then
        composeTestRule.onNodeWithContentDescription(expectedTagName).assertExists()
    }

    @Test
    fun when_key_composable_is_displayed__composable_frame_matches_design_requirements()
    {
        // given
        lateinit var expectedButtonShape: RoundedCornerShape
        var expectedButtonWidth: Dp? = null
        var expectedButtonHeight: Dp? = null

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                BoxWithConstraints()
                {
                    expectedButtonShape = RoundedCornerShape(corner = CornerSize(size = this.maxHeight * (4 / Scene.idealFrame().height)))
                    expectedButtonWidth = this.maxWidth * (33 / Scene.idealFrame().width)
                    expectedButtonHeight = this.maxHeight * (56 / Scene.idealFrame().height)
                }

                GameSceneKeyboard.Key.Component()
            }
        }

        // then
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(GameSceneKeyboard.Key.ButtonShapeKey, expectedButtonShape)
        ).assertExists()
        composeTestRule.onNodeWithContentDescription(
            GameSceneKeyboard.TagName.KEY.toString()
        ).assertWidthIsEqualTo(expectedButtonWidth!!)
        composeTestRule.onNodeWithContentDescription(
            GameSceneKeyboard.TagName.KEY.toString()
        ).assertHeightIsEqualTo(expectedButtonHeight!!)
    }

    @Test
    fun when_composable_invoked_with_letters__expected_text_is_displayed()
    {
        // given
        val expectedLetter = "B"

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                GameSceneKeyboard.Key.Component(
                    options = GameSceneKeyboard.Key.ComponentOptions(letters = expectedLetter)
                )
            }
        }

        // then
        composeTestRule.onNodeWithContentDescription(
            GameSceneKeyboard.TagName.KEY.toString(), useUnmergedTree = true
        ).onChild().assertTextEquals(expectedLetter)
    }

    @Test
    fun when_composable_invoked_with_ENTER_letters__composable_shape_matches_design_requirements()
    {
        // given
        var expectedButtonWidth: Dp? = null
        var expectedButtonHeight: Dp? = null

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                BoxWithConstraints()
                {
                    expectedButtonWidth = this.maxWidth * (52 / Scene.idealFrame().width)
                    expectedButtonHeight = this.maxHeight * (56 / Scene.idealFrame().height)
                }

                GameSceneKeyboard.Key.Component(
                    options = GameSceneKeyboard.Key.ComponentOptions(letters = "ENTER")
                )
            }
        }

        // then
        composeTestRule.onNodeWithContentDescription(
            GameSceneKeyboard.TagName.KEY.toString(), useUnmergedTree = true
        ).assertWidthIsEqualTo(expectedButtonWidth!!)
        composeTestRule.onNodeWithContentDescription(
            GameSceneKeyboard.TagName.KEY.toString(), useUnmergedTree = true
        ).assertHeightIsEqualTo(expectedButtonHeight!!)
    }

    @Test
    fun when_key_composable_invoked_with_resourceId__composable_matches_design_requirements()
    {
        // given
        var expectedButtonWidth: Dp? = null
        var expectedButtonHeight: Dp? = null
        var expectedImageSize: Dp? = null

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                BoxWithConstraints()
                {
                    expectedButtonWidth = this.maxWidth * (52 / Scene.idealFrame().width)
                    expectedButtonHeight = this.maxHeight * (56 / Scene.idealFrame().height)
                    expectedImageSize = this.maxHeight * (23 / Scene.idealFrame().height)
                }

                GameSceneKeyboard.Key.Component(
                    options = GameSceneKeyboard.Key.ComponentOptions(resourceId = "game_image_backspace")
                )
            }
        }

        // then
        composeTestRule.onNodeWithContentDescription(
            GameSceneKeyboard.TagName.KEY.toString(), useUnmergedTree = true
        ).assertWidthIsEqualTo(expectedButtonWidth!!)
        composeTestRule.onNodeWithContentDescription(
            GameSceneKeyboard.TagName.KEY.toString(), useUnmergedTree = true
        ).assertHeightIsEqualTo(expectedButtonHeight!!)
        composeTestRule.onNodeWithContentDescription(
            GameSceneKeyboard.TagName.KEY.toString(), useUnmergedTree = true
        ).onChild().assertHeightIsEqualTo(expectedImageSize!!)
    }

    @Test
    fun when_default_key_composable_invoked__color_matches_design_requirements()
    {
        // given
        lateinit var expectedButtonColors: ButtonColors

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                expectedButtonColors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background
                )

                GameSceneKeyboard.Key.Component(
                    options = GameSceneKeyboard.Key.ComponentOptions(
                        backgroundColor = GameKeyboard.Key.BackgroundColor.DEFAULT
                    )
                )
            }
        }

        // then
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(GameSceneKeyboard.Key.ButtonColorsKey, expectedButtonColors)
        ).assertExists()
    }
}