package com.megabreezy.breezybuilds_wordle.feature.game.presentation

import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.Announcement
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class GameSceneViewModelTests: KoinComponent
{
    private lateinit var answerLocalDataSource: MockAnswerLocalDataSource

    @BeforeTest
    fun setUp()
    {
        answerLocalDataSource = MockAnswerLocalDataSource()

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
        answerLocalDataSource.getAnswerShouldFail = true
        val handler = MockSceneHandler()
        val sut = GameSceneViewModel()

        // when
        sut.setUp(handler = handler)

        // then
        assertNotNull(answerLocalDataSource.answerToPut)
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
        sut.setUp()

        // when
        val actualGameBoard = sut.getGameBoard()

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
        sut.getGameKeyboard().rows().first().first().click()

        // then
        assertTrue(keyPressDidInvoke)
    }

    @Test
    fun `when the getAnnouncement method is invoked on an instance - the getAnnouncement use case is invoked`()
    {
        // given
        val expectedAnnouncement: Announcement by inject()
        val sut = GameSceneViewModel()
        expectedAnnouncement.setMessage(newMessage = "Testing a new announcement!")

        // when
        val actualAnnouncement = sut.getAnnouncement()

        // then
        assertEquals(expectedAnnouncement, actualAnnouncement)
    }

    class MockSceneHandler: GameSceneHandleable
    {
        var onGameStartedDidInvoke = false
        var onStartingGameDidInvoke = false

        override fun onGameOver() { }
        override fun onGameStarted() { onGameStartedDidInvoke = true }
        override fun onGuessFailed() { }
        override fun onGuessingWord() { }
        override fun onRevealNextTile() { }
        override fun onRoundCompleted() { }
        override fun onStartingGame() { onStartingGameDidInvoke = true }
    }

    class MockAnswerLocalDataSource: AnswerLocalDataManageable
    {
        var answerToPut: Answer? = null
        var getAnswerShouldFail = false

        override fun put(newAnswer: Answer): Answer
        {
            answerToPut = newAnswer

            return newAnswer
        }

        override fun getCurrent(): Answer
        {
            if (getAnswerShouldFail) throw AnswerNotFoundLocalDataException("Not Found.")

            return Answer(word = Word(word = "NOT_IN_USE"))
        }

        override fun getPrevious(): List<Answer> = listOf()

        override fun update(existingAnswer: Answer) = existingAnswer
    }
}