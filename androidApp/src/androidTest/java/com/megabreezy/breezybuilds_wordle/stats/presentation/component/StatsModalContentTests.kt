package com.megabreezy.breezybuilds_wordle.stats.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.stats.presentation.component.StatsModalContent
import com.megabreezy.breezybuilds_wordle.android.util.theme.ThemeFonts
import com.megabreezy.breezybuilds_wordle.android.util.theme.dpToSp
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StatsModalContentTests
{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_stat_component_is_displayed_with_content__the_composable_matches_design_requirements()
    {
        // given
        val expectedStatHeadline = "200"
        val expectedStatDescription = "Max Streak"
        lateinit var expectedHeadlineTextStyle: TextStyle
        lateinit var expectedDescriptionTextStyle: TextStyle

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                expectedHeadlineTextStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = ThemeFonts.roboto,
                    fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(40 / Scene.idealFrame().height)),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )

                expectedDescriptionTextStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = ThemeFonts.roboto,
                    fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(14 / Scene.idealFrame().height)),
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )

                StatsModalContent.Stat.Component(
                    options = StatsModalContent.Stat.ComponentOptions(
                        headline = expectedStatHeadline,
                        description = expectedStatDescription
                    )
                )
            }
        }
        val displayedStat = composeTestRule.onNodeWithContentDescription("${StatsModalContent.Stat.TagName.STAT}", useUnmergedTree = true)

        // then
        listOf(expectedStatHeadline, expectedStatDescription).forEachIndexed()
        { index, text ->
            displayedStat.onChildAt(index = index).assertTextEquals(text)
        }
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(StatsModalContent.Stat.HeadlineTextStyleKey, expectedHeadlineTextStyle)
        ).assertExists()
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(StatsModalContent.Stat.DescriptionTextStyleKey, expectedDescriptionTextStyle)
        ).assertExists()
    }

    @Test
    fun when_component_is_displayed_with_content__the_composable_matches_design_requirements()
    {
        // given

        // when

        // then
    }
}