package com.megabreezy.breezybuilds_wordle.feature.game.presentation

import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.mock.AnswerLocalDataSourceCommonMock
import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.AnnouncementRepresentable
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard
import com.megabreezy.breezybuilds_wordle.feature.game.presentation.mock.GameSceneHandlerCommonMock
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class GameSceneViewModelTests: KoinComponent
{
    private lateinit var answerLocalDataSource: AnswerLocalDataSourceCommonMock

    @BeforeTest
    fun setUp()
    {
        answerLocalDataSource = AnswerLocalDataSourceCommonMock()

        startKoin()
        {
            modules(
                CoreKoinModule(scenarios = listOf(Scenario.WORD_FOUND, Scenario.ANSWER_SAVED)).mockModule(),
                GameKoinModule().module(),
                module { single<AnswerLocalDataManageable> { answerLocalDataSource } }
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `when setup method is invoked on an instance with a handler - the setUpGameEvents use case is invoked`()
    {
        // given
        answerLocalDataSource.getCurrentAnswerShouldFail = true
        val handler = GameSceneHandlerCommonMock()
        val sut = GameSceneViewModel()

        // when
        runBlocking { sut.setUp(gameSceneHandler = handler) }

        // then
        assertNotNull(answerLocalDataSource.putNewAnswerToReturn)
        assertTrue(handler.onStartingGameDidInvoke)
        assertTrue(handler.onGameStartedDidInvoke)
    }

    @Test
    fun `when getHeader method is invoked on an instance - the getHeader use case is invoked`()
    {
        // given
        val expectedHeaderTitle = "WORDLE"
        val sut = GameSceneViewModel()

        // when
        val actualHeader = sut.getHeader()

        // then
        assertEquals(expectedHeaderTitle, actualHeader.title())
    }

    @Test
    fun `when the getGameBoard method is invoked on an instance - the getGameBoard use case is invoked`()
    {
        // given
        val sut = GameSceneViewModel()
        val expectedGameBoard: GameBoard by inject()
        runBlocking { sut.setUp() }

        // when
        val actualGameBoard = runBlocking { sut.getGameBoard() }

        // then
        assertEquals(expectedGameBoard, actualGameBoard)
    }

    @Test
    fun `when the getGameKeyboard method is invoked on an instance - the getGameKeyboard use case is invoked`()
    {
        // given
        val sut = GameSceneViewModel()
        val expectedKeyboard: GameKeyboard by inject()
        var keyPressDidInvoke = false
        expectedKeyboard.rows().first().first().setOnClick { keyPressDidInvoke = true }

        // when
        runBlocking { sut.getGameKeyboard().rows().first().first().click() }

        // then
        assertTrue(keyPressDidInvoke)
    }

    @Test
    fun `when the getAnnouncement method is invoked on an instance - the getAnnouncement use case is invoked`()
    {
        // given
        val expectedAnnouncement: AnnouncementRepresentable by inject()
        val sut = GameSceneViewModel()
        expectedAnnouncement.setMessage(newMessage = "Testing a new announcement!")

        // when
        val actualAnnouncement = sut.getAnnouncement()

        // then
        assertEquals(expectedAnnouncement, actualAnnouncement)
    }
}