package com.megabreezy.breezybuilds_wordle.stats.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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

    @Test
    fun when_displaying_component__expected_tagName_is_applied()
    {
        // given
        val expectedTagName = "stats_guess_distribution_component"

        // when
        composeTestRule.setContent { SceneMock.display { StatsGuessDistribution.Component() } }

        // then
        composeTestRule.onNodeWithContentDescription(expectedTagName).assertExists()
    }

    @Test
    fun when_displaying_component_with_title_and_rows__composable_matches_design_requirements()
    {
        // given
        val expectedTitle = "Guess Distribution"
        lateinit var titleTextStyle: TextStyle
        val expectedRows = listOf(
            listOf("10", "101"),
            listOf("11", "200")
        )
        var expectedTitleBottomPadding: Dp? = null

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                titleTextStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = ThemeFonts.roboto,
                    fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(20 / Scene.idealFrame().height)),
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
                expectedTitleBottomPadding = LocalSceneDimensions.current.height.times(20 / Scene.idealFrame().height)

                StatsGuessDistribution.Component(
                    options = StatsGuessDistribution.ComponentOptions(
                        title = expectedTitle,
                        rows = expectedRows.map()
                        { row ->
                            {
                                StatsGuessDistribution.GraphRow.Component(
                                    options = StatsGuessDistribution.GraphRow.ComponentOptions(
                                        round = row[0],
                                        correctGuessCount = row[1]
                                    )
                                )
                            }
                        }
                    )
                )
            }
        }
        val displayedColumn = composeTestRule.onNodeWithContentDescription("${StatsGuessDistribution.TagName.COlUMN}", useUnmergedTree = true)

        // then
        displayedColumn.assertIsDisplayed()
        displayedColumn.onChildAt(index = 0).assertTextEquals(expectedTitle)
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(StatsGuessDistribution.TitleTextStyleKey, titleTextStyle)
        ).assertExists()
        displayedColumn.onChildAt(index = 1).assertContentDescriptionEquals("spacer")
        displayedColumn.onChildAt(index = 1).assertHeightIsEqualTo(expectedTitleBottomPadding!!)
        expectedRows.forEachIndexed()
        { index, row ->
            displayedColumn.onChildAt(index = index + 2).assertContentDescriptionEquals("${StatsGuessDistribution.GraphRow.TagName.ROW}")
            displayedColumn.onChildAt(index = index + 2).onChildAt(index = 0).assertTextEquals(row[0])
            displayedColumn.onChildAt(index = index + 2).onChildAt(index = 1).assertTextEquals(row[1])
        }
    }
}