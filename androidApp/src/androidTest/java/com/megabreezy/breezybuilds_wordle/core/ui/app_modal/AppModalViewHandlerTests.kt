package com.megabreezy.breezybuilds_wordle.core.ui.app_modal

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.core.ui.app_modal.AppModalViewHandler
import com.megabreezy.breezybuilds_wordle.android.core.ui.app_modal.rememberAppModalViewHandler
import com.megabreezy.breezybuilds_wordle.android.stats.presentation.component.StatsGuessDistribution
import com.megabreezy.breezybuilds_wordle.android.stats.presentation.component.StatsModalContent
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.mock.AppModalCommonMock
import com.megabreezy.breezybuilds_wordle.core.ui.button.ButtonRepresentable
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.GuessDistribution
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.Stat
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.StatsModal
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppModalViewHandlerTests
{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_when_setUp_invoked__AppModal__setHandler_method_is_invoked_passing_in_self()
    {
        // given
        val mockModal = AppModalCommonMock()
        val sut = AppModalViewHandler(appModal = mockModal)

        // when
        sut.setUp()

        // then
        Assert.assertNotNull(mockModal.setHandlerPassedInNewHandler)
    }

    @Test
    fun test_when_onModalShouldShow_invoked__expected_composable_is_displayed()
    {
        // given
        lateinit var handler: AppModalViewHandler
        val modalHelper = MockSharedModalHelper()
        val mockModal = AppModalCommonMock()
        val expectedModalContent = modalHelper.mockStatsModal()
        mockModal.contentToReturn = expectedModalContent

        // when
        composeTestRule.setContent()
        {
            handler = rememberAppModalViewHandler(appModal = mockModal, scope = rememberCoroutineScope())

            SceneMock.display()
            {
                MockView().Component(handler = handler)
            }
        }
        mockModal.handlerToReturn = handler
        mockModal.handler()?.onModalShouldShow(animationDuration = 0L)
        val displayedMockView = composeTestRule.onNodeWithContentDescription("MOCK_COLUMN", useUnmergedTree = true).onChildAt(index = 0)
        composeTestRule.onNodeWithContentDescription("${StatsModalContent.PlayAgainButton.TagName.BUTTON}").performClick()

        // then
        displayedMockView.onChildAt(index = 0).assertContentDescriptionEquals("${StatsModalContent.TagName.CONTAINER}")
        composeTestRule.onAllNodesWithContentDescription(
            "${StatsModalContent.Stat.TagName.STAT}"
        ).assertCountEquals(modalHelper.statsToReturn().count())
        composeTestRule.onAllNodesWithContentDescription(
            "${StatsGuessDistribution.GraphRow.TagName.ROW}"
        ).assertCountEquals(modalHelper.guessDistributionToReturn().rows().count())
        Assert.assertTrue(modalHelper.playAgainButtonClicked)
    }

    @Test
    fun test_when_onModalShouldHide_invoked__expected_view_is_not_displayed()
    {
        lateinit var handler: AppModalViewHandler
        val modalHelper = MockSharedModalHelper()
        val mockModal = AppModalCommonMock()
        val expectedModalContent = modalHelper.mockStatsModal()
        mockModal.contentToReturn = expectedModalContent

        // when
        composeTestRule.setContent()
        {
            handler = rememberAppModalViewHandler(appModal = mockModal)

            SceneMock.display()
            {
                MockView().Component(handler = handler)
            }
        }
        mockModal.handlerToReturn = handler
        mockModal.handler()?.onModalShouldShow(animationDuration = 0L)
        mockModal.handler()?.onModalShouldHide(animationDuration = 0L)

        // then
        composeTestRule.onNodeWithContentDescription("MOCK_COLUMN", useUnmergedTree = true).onChildAt(index = 0).assertDoesNotExist()
    }

    class MockView
    {
        @Composable
        fun Component(handler: AppModalViewHandler)
        {
            LaunchedEffect(Unit) { handler.setUp() }

            Column(modifier = Modifier.semantics { contentDescription = "MOCK_COLUMN" })
            {
                if (handler.appModalIsShowing) handler.ModalContent()
            }
        }
    }

    class MockSharedModalHelper(
        var closeButtonClicked: Boolean = false,
        var playAgainButtonClicked: Boolean = false,
    )
    {
        var closeButton: Button
        var playAgainButton: Button
        var closeButtonResourceId: String = "fake_close_image"
        var playAgainButtonLabel: String = "play"

        init
        {
            closeButton = Button(
                mockResourceId = closeButtonResourceId
            ) { closeButtonClicked = true }

            playAgainButton = Button(
                mockLabel = playAgainButtonLabel
            ) { playAgainButtonClicked = true }
        }

        fun statsToReturn(): List<Stat> = listOf(
            Stat(headline = "10", description = "Something"),
            Stat(headline = "20", description = "Cool!")
        )

        fun guessDistributionToReturn(): GuessDistribution = GuessDistribution(
            title = "Guess Distribution",
            rows = listOf(
                GuessDistribution.Row(round = 1, correctGuessesCount = 2),
                GuessDistribution.Row(round = 2, correctGuessesCount = 3)
            )
        )

        fun mockStatsModal(): StatsModal = StatsModal(
            closeButton = closeButton,
            stats = statsToReturn(),
            guessDistribution = guessDistributionToReturn(),
            playAgainButton = playAgainButton
        )

        class Button(
            var mockResourceId: String? = null,
            var mockLabel: String? = null,
            var onClick: suspend () -> Unit
        ): ButtonRepresentable
        {
            override suspend fun click() = onClick()
            override fun imageResourceId() = this.mockResourceId
            override fun label() = this.mockLabel
        }
    }
}