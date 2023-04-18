package com.megabreezy.breezybuilds_wordle.stats.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.core.ui.Scene
import com.megabreezy.breezybuilds_wordle.android.core.util.LocalSceneDimensions
import com.megabreezy.breezybuilds_wordle.android.stats.presentation.component.StatsGuessDistribution
import com.megabreezy.breezybuilds_wordle.android.util.theme.ThemeFonts
import com.megabreezy.breezybuilds_wordle.android.util.theme.dpToSp
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StatsGuessDistributionTests
{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun when_row_component_is_displayed__the_expected_TagName_is_applied()
    {
        // given
        val expectedTagName = "stats_guess_distribution_component_row"

        // when
        composeTestRule.setContent { SceneMock.display { StatsGuessDistribution.GraphRow.Component() } }

        // then
        composeTestRule.onNodeWithContentDescription(expectedTagName).assertExists()
    }

    @Test
    fun when_row_component_is_displayed_with_round_and_guessCount__composable_matches_design_requirements()
    {
        // given
        lateinit var expectedRowTextStyle: TextStyle
        val expectedRoundText = "10"
        val expectedCorrectGuessCountText = "104"

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                expectedRowTextStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = ThemeFonts.roboto,
                    fontWeight = FontWeight.Normal,
                    fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(20 / Scene.idealFrame().height)),
                    textAlign = TextAlign.Right
                )

                StatsGuessDistribution.GraphRow.Component(
                    options = StatsGuessDistribution.GraphRow.ComponentOptions(
                        round = expectedRoundText,
                        correctGuessCount = expectedCorrectGuessCountText
                    )
                )
            }
        }
        val displayedRow = composeTestRule.onNodeWithContentDescription("${StatsGuessDistribution.GraphRow.TagName.ROW}", useUnmergedTree = true)

        // then
        listOf(expectedRoundText, expectedCorrectGuessCountText).forEachIndexed()
        { index, text ->
            displayedRow.onChildAt(index = index).assertTextEquals(text)
        }
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(StatsGuessDistribution.GraphRow.TextStyleKey, expectedRowTextStyle)
        ).assertExists()
    }
}