package com.megabreezy.breezybuilds_wordle.stats.presentation.component

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.stats.presentation.component.StatsGuessDistribution
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
    fun when_composable_is_displayed__the_expected_TagName_is_applied()
    {
        // given
        val expectedTagName = "stats_guess_distribution_component_row"

        // when
        composeTestRule.setContent { SceneMock.display { StatsGuessDistribution.GraphRow.Component() } }

        // then
        composeTestRule.onNodeWithContentDescription(expectedTagName).assertExists()
    }
}