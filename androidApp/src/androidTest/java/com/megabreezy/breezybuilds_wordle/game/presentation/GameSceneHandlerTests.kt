package com.megabreezy.breezybuilds_wordle.game.presentation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.game.presentation.GameSceneHandler
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneAnnouncement
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneBoard
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneHeader
import com.megabreezy.breezybuilds_wordle.android.game.presentation.component.GameSceneKeyboard
import com.megabreezy.breezybuilds_wordle.android.game.presentation.rememberGameSceneHandler
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import com.megabreezy.breezybuilds_wordle.core.util.KoinPlatformManager
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.feature.game.presentation.GameSceneViewModel
import kotlinx.coroutines.runBlocking
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
        runBlocking { sut.setUp() }

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
            SceneMock.display { sut.GameHeader() }
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
        val expectedRowCount = runBlocking { GameSceneViewModel().getGameBoard().rows().count() }
        val expectedTilesCount = runBlocking { expectedRowCount * (GameSceneViewModel().getGameBoard().rows().last().count()) }

        // when
        composeTestRule.setContent()
        {
            handler = rememberGameSceneHandler(scope = rememberCoroutineScope())

            SceneMock.display { handler.GameBoard() }
        }

        runBlocking()
        {
            handler.setUp()
        }

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

            SceneMock.display { handler.GameKeyboard() }
        }

        runBlocking { handler.setUp() }

        // then
        composeTestRule.onNodeWithContentDescription(
            GameSceneKeyboard.TagName.ROWS.toString(), useUnmergedTree = true
        ).onChildAt(index = expectedKeyCount - 1).assertExists()
    }

    @Test
    fun when_GameAnnouncement_view_appears__text_matches_expected_value()
    {
        // given
        lateinit var handler: GameSceneHandler
        val expectedTextValue = "My Middle Layer Announcement"
        val actualAnnouncement = GameSceneViewModel().getAnnouncement()
        actualAnnouncement.setMessage(newMessage = expectedTextValue)

        // when
        composeTestRule.setContent()
        {
            handler = rememberGameSceneHandler()

            SceneMock.display { handler.GameAnnouncement() }
        }

        // then
        composeTestRule.onNodeWithContentDescription(
            GameSceneAnnouncement.TagName.COMPONENT.toString(), useUnmergedTree = true
        ).assertTextEquals(expectedTextValue)
    }

    @Test
    fun when_onRevealNextTile_invoked__active_view_is_published_to_GAME()
    {
        // given
        lateinit var handler: GameSceneHandler
        val actualAnnouncement = GameSceneViewModel().getAnnouncement()

        // when
        composeTestRule.setContent()
        {
            handler = rememberGameSceneHandler()

            LaunchedEffect(Unit) { handler.setUp() }

            SceneMock.display { handler.GameAnnouncement() }
        }
        actualAnnouncement.setMessage(newMessage = "Revealing next tile!")
        handler.onRevealNextTile()

        // then
        Assert.assertEquals(GameSceneHandler.ViewType.GAME, handler.activeView)
        Assert.assertTrue(handler.gameKeyboardIsEnabled)
        composeTestRule.onNodeWithContentDescription(
            GameSceneAnnouncement.TagName.COMPONENT.toString(), useUnmergedTree = true
        ).assertTextEquals("Revealing next tile!")
    }

    @Test
    fun when_onRoundCompleted_invoked__active_view_is_published_to_GAME()
    {
        // given
        lateinit var handler: GameSceneHandler
        val actualAnnouncement = GameSceneViewModel().getAnnouncement()

        // when
        composeTestRule.setContent()
        {
            handler = rememberGameSceneHandler()

            LaunchedEffect(Unit) { handler.setUp() }

            SceneMock.display { handler.GameAnnouncement() }
        }
        actualAnnouncement.setMessage(newMessage = "Next Round!")
        handler.activeView = GameSceneHandler.ViewType.EMPTY
        handler.onRoundCompleted()

        // then
        Assert.assertEquals(GameSceneHandler.ViewType.GAME, handler.activeView)
        Assert.assertTrue(handler.gameKeyboardIsEnabled)
        composeTestRule.onNodeWithContentDescription(
            GameSceneAnnouncement.TagName.COMPONENT.toString(), useUnmergedTree = true
        ).assertTextEquals("Next Round!")
    }

    @Test
    fun when_onStartingGame_invoked__active_view_is_published_to_GAME()
    {
        // given
        lateinit var handler: GameSceneHandler
        val actualAnnouncement = GameSceneViewModel().getAnnouncement()

        // when
        composeTestRule.setContent()
        {
            handler = rememberGameSceneHandler()

            LaunchedEffect(Unit) { handler.setUp() }

            SceneMock.display { handler.GameAnnouncement() }
        }
        actualAnnouncement.setMessage(newMessage = "Start Game!")
        handler.activeView = GameSceneHandler.ViewType.EMPTY
        handler.onStartingGame()

        // then
        Assert.assertEquals(GameSceneHandler.ViewType.GAME, handler.activeView)
        Assert.assertFalse(handler.gameKeyboardIsEnabled)
        composeTestRule.onNodeWithContentDescription(
            GameSceneAnnouncement.TagName.COMPONENT.toString(), useUnmergedTree = true
        ).assertTextEquals("Start Game!")
    }

    @Test
    fun when_onGuessingWord_invoked__active_view_is_published_to_GAME()
    {
        // given
        lateinit var handler: GameSceneHandler
        val actualAnnouncement = GameSceneViewModel().getAnnouncement()

        // when
        composeTestRule.setContent()
        {
            handler = rememberGameSceneHandler()

            LaunchedEffect(Unit) { handler.setUp() }

            SceneMock.display { handler.GameAnnouncement() }
        }
        actualAnnouncement.setMessage(newMessage = "Guessing word, hold your ponies!")
        handler.activeView = GameSceneHandler.ViewType.EMPTY
        handler.onGuessingWord()

        // then
        Assert.assertEquals(GameSceneHandler.ViewType.GAME, handler.activeView)
        Assert.assertFalse(handler.gameKeyboardIsEnabled)
        composeTestRule.onNodeWithContentDescription(
            GameSceneAnnouncement.TagName.COMPONENT.toString(), useUnmergedTree = true
        ).assertTextEquals("Guessing word, hold your ponies!")
    }

    @Test
    fun when_onAnnouncementShouldShow_invoked__active_view_is_published_to_GAME()
    {
        // given
        lateinit var handler: GameSceneHandler
        val actualAnnouncement = GameSceneViewModel().getAnnouncement()

        // when
        composeTestRule.setContent()
        {
            handler = rememberGameSceneHandler()

            LaunchedEffect(Unit) { handler.setUp() }

            SceneMock.display { handler.GameAnnouncement() }
        }
        actualAnnouncement.setMessage(newMessage = "Not in words list!")
        handler.activeView = GameSceneHandler.ViewType.EMPTY
        handler.onAnnouncementShouldShow()

        // then
        Assert.assertEquals(GameSceneHandler.ViewType.GAME, handler.activeView)
        Assert.assertTrue(handler.gameKeyboardIsEnabled)
        composeTestRule.onNodeWithContentDescription(
            GameSceneAnnouncement.TagName.COMPONENT.toString(), useUnmergedTree = true
        ).assertTextEquals("Not in words list!")
    }

    @Test
    fun when_onAnnouncementShouldHide_invoked__active_view_is_published_to_GAME()
    {
        // given
        lateinit var handler: GameSceneHandler
        val actualAnnouncement = GameSceneViewModel().getAnnouncement()
        actualAnnouncement.setMessage(newMessage = "This message should be hidden!")

        // when
        composeTestRule.setContent()
        {
            handler = rememberGameSceneHandler()

            LaunchedEffect(Unit) { handler.setUp() }

            SceneMock.display { handler.GameAnnouncement() }
        }
        actualAnnouncement.setMessage(newMessage = null)
        handler.activeView = GameSceneHandler.ViewType.EMPTY
        handler.onAnnouncementShouldHide()

        // then
        Assert.assertEquals(GameSceneHandler.ViewType.GAME, handler.activeView)
        Assert.assertTrue(handler.gameKeyboardIsEnabled)
        composeTestRule.onNodeWithContentDescription(
            GameSceneAnnouncement.TagName.COMPONENT.toString(), useUnmergedTree = true
        ).assertDoesNotExist()
    }

    @Test
    fun when_onGameOver_invoked__active_view_is_published_to_GAME()
    {
        // given
        lateinit var handler: GameSceneHandler
        val actualAnnouncement = GameSceneViewModel().getAnnouncement()

        // when
        composeTestRule.setContent()
        {
            handler = rememberGameSceneHandler()

            LaunchedEffect(Unit) { handler.setUp() }

            SceneMock.display { handler.GameAnnouncement() }
        }
        actualAnnouncement.setMessage(newMessage = "Game over, man!")
        handler.activeView = GameSceneHandler.ViewType.EMPTY
        handler.onGameOver()

        // then
        Assert.assertEquals(GameSceneHandler.ViewType.GAME, handler.activeView)
        Assert.assertFalse(handler.gameKeyboardIsEnabled)
        composeTestRule.onNodeWithContentDescription(
            GameSceneAnnouncement.TagName.COMPONENT.toString(), useUnmergedTree = true
        ).assertTextEquals("Game over, man!")
    }

    @Test
    fun when_gameKeyboard_is_enabled_and_key_clicked__expected_function_is_invoked()
    {
        // given
        lateinit var handler: GameSceneHandler

        // when
        composeTestRule.setContent()
        {
            handler = rememberGameSceneHandler(scope = rememberCoroutineScope())

            LaunchedEffect(Unit) { handler.setUp() }

            SceneMock.display { handler.GameKeyboard() }
        }
        runBlocking { handler.setUp() }
        handler.activeView = GameSceneHandler.ViewType.EMPTY
        composeTestRule.onAllNodesWithContentDescription(
            GameSceneKeyboard.TagName.KEY.toString()
        ).onFirst().performClick()

        // then
        Assert.assertEquals(GameSceneHandler.ViewType.GAME, handler.activeView)
    }

    @Test
    fun when_gameKeyboard_is_not_enabled_and_key_clicked__no_function_is_invoked()
    {
        // given
        lateinit var handler: GameSceneHandler

        // when
        composeTestRule.setContent()
        {
            handler = rememberGameSceneHandler()

            LaunchedEffect(Unit) { handler.setUp() }

            SceneMock.display { handler.GameKeyboard() }
        }
        runBlocking { handler.setUp() }
        handler.onGuessingWord()
        handler.activeView = GameSceneHandler.ViewType.EMPTY
        composeTestRule.onAllNodesWithContentDescription(
            GameSceneKeyboard.TagName.KEY.toString()
        ).onFirst().performClick()

        // then
        Assert.assertEquals(GameSceneHandler.ViewType.EMPTY, handler.activeView)
    }
}