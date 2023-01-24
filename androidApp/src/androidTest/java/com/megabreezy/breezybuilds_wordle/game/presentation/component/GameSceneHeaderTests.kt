package com.megabreezy.breezybuilds_wordle.game.presentation.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneHeader
import com.megabreezy.breezybuilds_wordle.android.util.theme.ThemeFonts
import com.megabreezy.breezybuilds_wordle.android.util.theme.dpToSp
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

    @Test
    fun when_displaying_composable_with_title__title_matches_design_requirements()
    {
        // given
        lateinit var expectedTextStyle: TextStyle
        val expectedTitleText = "My Awesome Game"

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                BoxWithConstraints(modifier = Modifier.fillMaxSize())
                {
                    expectedTextStyle = TextStyle(
                        color = MaterialTheme.colors.onPrimary,
                        fontFamily = ThemeFonts.roboto,
                        fontWeight = FontWeight.Black,
                        fontSize = dpToSp(dp = this.maxHeight * (40 / Scene.idealFrame().height)),
                        lineHeight = dpToSp(this.maxHeight * (46.88f / Scene.idealFrame().height)),
                        letterSpacing = (0.1).sp
                    )

                    GameSceneHeader.Component(options = GameSceneHeader.ComponentOptions(text = expectedTitleText))
                }
            }
        }

        // then
        composeTestRule.onNodeWithContentDescription(
            GameSceneHeader.TagName.COMPONENT.toString()
        ).onChild().assertTextEquals(expectedTitleText)

        composeTestRule.onNode(
            SemanticsMatcher.expectValue(GameSceneHeader.TextStyleKey, expectedTextStyle)
        ).assertExists()
    }
}