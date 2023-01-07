package com.megabreezy.breezybuilds_wordle.feature.game.presentation

import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class GameSceneViewModelTests
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
    fun `when setup method is invoked on an instance - the setUpGameEvents use case is invoked`()
    {
        // given
        answerLocalDataSource.getAnswerShouldFail = true
        val sut = GameSceneViewModel()

        // when
        sut.setUp()

        // then
        assertNotNull(answerLocalDataSource.answerToPut)
    }

    @Test
    fun `when getHeader method is invoked on an instance - the getHeader use case is invoked`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when the getGameBoard method is invoked on an instance - the getGameBoard use case is invoked`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when the getGameKeyboard method is invoked on an instance - the getGameKeyboard use case is invoked`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when the getAnnouncement method is invoked on an instance - the getAnnouncement use case is invoked`()
    {
        // given

        // when

        // then
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