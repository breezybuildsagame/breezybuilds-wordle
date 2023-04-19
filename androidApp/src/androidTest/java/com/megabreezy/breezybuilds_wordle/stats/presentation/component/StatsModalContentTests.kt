package com.megabreezy.breezybuilds_wordle.stats.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
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
import org.junit.Assert
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
        val expectedStats = listOf(
            listOf("100", "Max Streak"),
            listOf("40", "Win %")
        )
        val distributionTitle = "Test Distribution"
        val expectedButtonLabel = "Play Again"
        var buttonWasClicked = false
        lateinit var expectedTitleTextStyle: TextStyle

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                expectedTitleTextStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = ThemeFonts.roboto,
                    fontWeight = FontWeight.Black,
                    fontSize = dpToSp(dp = LocalSceneDimensions.current.height.times(20 / Scene.idealFrame().height))
                )

                StatsModalContent.Component(
                    options = StatsModalContent.ComponentOptions(
                        statsRow = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(LocalSceneDimensions.current.width.times(5 / Scene.idealFrame().width)),
                                modifier = Modifier
                                    .semantics { contentDescription = "${StatsModalContent.TagName.ROW}" },
                                verticalAlignment = Alignment.Top
                            )
                            {
                                expectedStats.map()
                                { stat ->
                                    StatsModalContent.Stat.Component(
                                        options = StatsModalContent.Stat.ComponentOptions(
                                            modifier = Modifier.weight(1f),
                                            headline = stat[0],
                                            description = stat[1]
                                        )
                                    )
                                }
                            }
                        },
                        guessDistribution = { Text(text = distributionTitle) },
                        playAgainButton = {
                            StatsModalContent.PlayAgainButton.Component(
                                options = StatsModalContent.PlayAgainButton.ComponentOptions(
                                    label = expectedButtonLabel,
                                    onClick = { buttonWasClicked = true }
                                )
                            )
                        }
                    )
                )
            }
        }
        val displayedModalContent = composeTestRule.onNodeWithContentDescription("${StatsModalContent.TagName.CONTAINER}", useUnmergedTree = true)
        val displayedStatRow = displayedModalContent.onChildAt(index = 1)
        val displayedButton = composeTestRule.onNodeWithContentDescription("${StatsModalContent.PlayAgainButton.TagName.BUTTON}", useUnmergedTree = true)

        // then
        displayedModalContent.assertExists()
        displayedModalContent.onChildAt(index = 0).assertTextEquals("STATISTICS")
        composeTestRule.onNode(
            SemanticsMatcher.expectValue(StatsModalContent.TitleTextStyleKey, expectedTitleTextStyle)
        ).assertExists()
        displayedModalContent.onChildAt(index = 2).assertTextEquals(distributionTitle)
        displayedModalContent.onChildAt(index = 3).assertContentDescriptionEquals("${StatsModalContent.PlayAgainButton.TagName.BUTTON}")
        displayedButton.onChildAt(index = 0).assertTextEquals(expectedButtonLabel)
        displayedStatRow.assertExists()

        expectedStats.forEachIndexed()
        { index, stat ->
            val displayedStat = displayedStatRow.onChildAt(index = index)
            displayedStat.assertContentDescriptionEquals("${StatsModalContent.Stat.TagName.STAT}")
            displayedStat.onChildAt(index = 0).assertTextEquals(stat[0])
            displayedStat.onChildAt(index = 1).assertTextEquals(stat[1])
        }

        displayedButton.performClick()
        Assert.assertTrue(buttonWasClicked)
    }
}