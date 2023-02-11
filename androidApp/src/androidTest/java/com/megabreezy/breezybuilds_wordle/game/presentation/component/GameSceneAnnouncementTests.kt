package com.megabreezy.breezybuilds_wordle.game.presentation.component

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneAnnouncement
import com.megabreezy.breezybuilds_wordle.android.util.theme.ThemeFonts
import com.megabreezy.breezybuilds_wordle.android.util.theme.dpToSp
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameSceneAnnouncementTests
{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_composable_is_displayed__expected_tag_name_is_applied()
    {
        // given
        val expectedTagName = "game_scene_announcement_component"

        // when
        composeTestRule.setContent { SceneMock.display { GameSceneAnnouncement.Component() } }

        // then
        composeTestRule.onNodeWithContentDescription(expectedTagName).assertExists()
    }

    @Test
    fun when_composable_is_displayed__component_matches_design_requirements()
    {
        // given
        lateinit var expectedTextStyle: TextStyle
        lateinit var expectedSurfaceShape: Shape
        var expectedBackgroundColor: Color? = null
        var expectedPadding: Dp? = null
        val expectedText = "Custom Announcement Time!"

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                BoxWithConstraints()
                {
                    expectedTextStyle = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = ThemeFonts.roboto,
                        fontWeight = FontWeight.Bold,
                        fontSize = dpToSp(dp = this.maxHeight * (20 / Scene.idealFrame().height)),
                        textAlign = TextAlign.Center
                    )
                    expectedBackgroundColor = MaterialTheme.colorScheme.onPrimary
                    expectedPadding = this.maxHeight * (26 / Scene.idealFrame().height)
                    expectedSurfaceShape = RoundedCornerShape(
                        corner = CornerSize(size = this.maxHeight * (8 / Scene.idealFrame().height)))
                }

                GameSceneAnnouncement.Component(options = GameSceneAnnouncement.ComponentOptions(text = expectedText))
            }
        }

        // then
        composeTestRule.onNodeWithContentDescription(
            GameSceneAnnouncement.TagName.COMPONENT.toString()).assertTextEquals(expectedText)
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(GameSceneAnnouncement.TextStyleKey, expectedTextStyle)).assertExists()
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(GameSceneAnnouncement.TextPaddingKey, expectedPadding!!)).assertExists()
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(GameSceneAnnouncement.SurfaceBackgroundColorKey, expectedBackgroundColor!!)).assertExists()
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(GameSceneAnnouncement.SurfaceShapeKey, expectedSurfaceShape)).assertExists()
    }
}