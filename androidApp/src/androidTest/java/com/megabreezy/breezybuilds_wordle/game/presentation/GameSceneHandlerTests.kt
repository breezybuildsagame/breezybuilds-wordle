package com.megabreezy.breezybuilds_wordle.game.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.game.presentation.GameSceneHandler
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneBoard
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneHeader
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneKeyboard
import com.megabreezy.breezybuilds_wordle.android.game.presentation.rememberGameSceneHandler
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import com.megabreezy.breezybuilds_wordle.core.util.KoinPlatformManager
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.feature.game.presentation.GameSceneViewModel
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameSceneHandlerTests
{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp()
    {
        KoinPlatformManager.start(
            scenarios = listOf(
                Scenario.WORD_FOUND,
                Scenario.ANSWER_SAVED
            )
        )
    }

    @After
    fun tearDown() = KoinPlatformManager.stop()

    @Test
    fun when_initialized__activeView_is_Empty()
    {
        // given
        val expectedActiveView = GameSceneHandler.ViewType.EMPTY

        // when
        val sut = GameSceneHandler()

        // then
        Assert.assertEquals(expectedActiveView, sut.activeView)
        Assert.assertFalse(sut.gameKeyboardIsEnabled)
    }

    @Test
    fun when_setUp_invoked__viewModel_setUp_is_invoked()
    {
        // given
        val expectedActiveView = GameSceneHandler.ViewType.GAME
        val sut = GameSceneHandler()

        // when
        sut.setUp()

        // then
        Assert.assertEquals(expectedActiveView, sut.activeView)
        Assert.assertTrue(sut.gameKeyboardIsEnabled)
    }

    @Test
    fun when_GameHeader_view_appears__title_matches_expected_title()
    {
        // given
        val expectedTitle = GameSceneViewModel().getHeader().title()
        val sut = GameSceneHandler()

        // when
        composeTestRule.setContent()
        {
            SceneMock.display()
            {
                sut.GameHeader()
            }
        }

        // then
        composeTestRule.onNodeWithContentDescription(
            GameSceneHeader.TagName.COMPONENT.toString()
        ).onChild().assertTextEquals(expectedTitle)
    }

    @Test
    fun when_GameBoard_view_appears__rows_match_expected_value()
    {
        // given
        lateinit var handler: GameSceneHandler
        val expectedRowCount = GameSceneViewModel().getGameBoard().rows().count()
        val expectedTilesCount = expectedRowCount * (GameSceneViewModel().getGameBoard().rows().last().count())

        // when
        composeTestRule.setContent()
        {
            handler = rememberGameSceneHandler()

            SceneMock.display()
            {
                handler.GameBoard()
            }
        }

        handler.setUp()

        // then
        composeTestRule.onNodeWithContentDescription(
            GameSceneBoard.TagName.BOARD.toString(), useUnmergedTree = true
        ).onChildAt(index = expectedTilesCount - 1).assertExists()
    }

    @Test
    fun when_gameKeyboard_view_appears__rows_match_expected_value()
    {
        // given
        lateinit var handler: GameSceneHandler
        var expectedKeyCount = 0
        GameSceneViewModel().getGameKeyboard().rows().forEach { expectedKeyCount += it.count() }

        // when
        composeTestRule.setContent()
        {
            handler = rememberGameSceneHandler()

            SceneMock.display()
            {
                handler.GameKeyboard()
            }
        }

        handler.setUp()

        // then
        composeTestRule.onNodeWithContentDescription(
            GameSceneKeyboard.TagName.ROWS.toString(), useUnmergedTree = true
        ).onChildAt(index = expectedKeyCount - 1).assertExists()
    }
}