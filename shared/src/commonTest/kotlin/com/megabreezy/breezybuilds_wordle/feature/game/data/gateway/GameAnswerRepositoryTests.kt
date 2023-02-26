package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway

import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerPutFailedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerUpdateFailedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.word.mock.WordLocalDataSourceMock
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotCreatedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class GameAnswerRepositoryTests
{
    private lateinit var answerDataSource: MockAnswerLocalDataSource
    private lateinit var wordDataSource: MockWordLocalDataSource

    @BeforeTest
    fun setUp()
    {
        answerDataSource = MockAnswerLocalDataSource()
        wordDataSource = MockWordLocalDataSource()

        startKoin()
        {
            modules(
                CoreKoinModule(scenarios = listOf(Scenario.WORD_FOUND)).mockModule(),
                GameKoinModule().module(),
                module { single<WordLocalDataManageable> { wordDataSource } },
                module { single<AnswerLocalDataManageable> { answerDataSource } }
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `when create method is invoked - answer local data source get method is invoked`()
    {
        // given
        val sut = GameAnswerRepository()

        // when
        runBlocking { sut.create() }

        // then
        assertNotNull(answerDataSource.getCurrentAnswerToReturn)
    }

    @Test
    fun `when create method is invoked - word local data source get method is invoked - passing in expected parameter`()
    {
        // given
        val sut = GameAnswerRepository()
        val previousAnswers = listOf(
            Answer(word = Word(word = "MOCKS")),
            Answer(word = Word(word = "SOCKS")),
            Answer(word = Word(word = "ROCKS"))
        )
        answerDataSource.getPreviousAnswersToReturn = previousAnswers

        // when
        runBlocking { sut.create() }
        val expectedExcludingWords = listOf(
            Word(word = "MOCKS"),
            Word(word = "SOCKS"),
            Word(word = "ROCKS"),
            answerDataSource.getCurrentAnswerToReturn!!.word()
        )
        val actualExcludingWordsParameter = wordDataSource.excludingWords

        // then
        assertNotNull(wordDataSource.wordToReturn)
        assertEquals(expectedExcludingWords, actualExcludingWordsParameter)
    }

    @Test
    fun `when create method is invoked and answer data source returns a current answer - answer local data source update method is invoked - passing in expected updated answer`()
    {
        // given
        val sut = GameAnswerRepository()

        // when
        runBlocking { sut.create() }
        val expectedUpdatedAnswer = Answer(word = answerDataSource.getCurrentAnswerToReturn!!.word(), isCurrent = false)

        // then
        assertEquals(expectedUpdatedAnswer, answerDataSource.updatedAnswerToReturn)
    }

    @Test
    fun `when create method is invoked and answer data source throws update exception - expected exception is thrown`()
    {
        // given
        val expectedExceptionMessage = "Existing Answer not updated."
        val sut = GameAnswerRepository()
        answerDataSource.updateAnswerShouldFail = true

        // when
        val actualException = assertFailsWith<GameAnswerNotCreatedRepositoryException> { runBlocking { sut.create() } }

        assertEquals(expectedExceptionMessage, actualException.message)
    }

    @Test
    fun `when create method is invoked and answer local data source throws an exception - word local data source get method is invoked- passing in expected parameter`()
    {
        // given
        val sut = GameAnswerRepository()
        val previousAnswers = listOf(
            Answer(word = Word(word = "MOCKS")),
            Answer(word = Word(word = "SOCKS"))
        )
        answerDataSource.getCurrentAnswerShouldFail = true
        answerDataSource.getPreviousAnswersToReturn = previousAnswers

        // when
        runBlocking { sut.create() }
        val expectedExcludingWords = listOf(
            Word(word = "MOCKS"),
            Word(word = "SOCKS"),
        )
        val actualExcludingWordsParameter = wordDataSource.excludingWords

        // then
        assertNotNull(wordDataSource.wordToReturn)
        assertEquals(expectedExcludingWords, actualExcludingWordsParameter)
    }

    @Test
    fun `when creating answer and word local data source returns a word - expected GameAnswer is returned`()
    {
        // given
        val sut = GameAnswerRepository()

        // when
        val actualWord = runBlocking { sut.create() }
        val expectedWord = GameAnswer(word = wordDataSource.wordToReturn!!.word())

        // then
        assertEquals(expectedWord, actualWord)
    }

    @Test
    fun `when creating answer and word local data source returns a word - answer local data source put method is invoked passing in expected parameter`()
    {
        // given
        val sut = GameAnswerRepository()

        // when
        val actualGameAnswer = runBlocking { sut.create() }
        val expectedNewAnswer = Answer(word = wordDataSource.wordToReturn!!, isCurrent = true)
        val expectedGameAnswer = GameAnswer(word = wordDataSource.wordToReturn!!.word())

        // then
        assertEquals(expectedGameAnswer, actualGameAnswer)
        assertEquals(expectedNewAnswer, answerDataSource.putNewAnswerToReturn)
    }

    @Test
    fun `when creating answer and word local data source throws an exception - expected exception is thrown`()
    {
        // given
        val expectedExceptionMessage = "Not found."
        val sut = GameAnswerRepository()
        wordDataSource.getShouldFail = true

        // when
        val actualException = assertFailsWith<GameAnswerNotCreatedRepositoryException>
        {
            runBlocking { sut.create() }
        }

        // then
        assertEquals(expectedExceptionMessage, actualException.message)
    }

    @Test
    fun `When get method invoked - answer local data source getCurrent method is invoked`()
    {
        // given
        val sut = GameAnswerRepository()

        // when
        sut.get()

        // then
        assertNotNull(answerDataSource.getCurrentAnswerToReturn)
    }

    @Test
    fun `When get method invoked and answer data source returns an answer - expected GameAnswer is returned`()
    {
        // given
        val expectedGameAnswer = GameAnswer(word = "SLAYS")
        val sut = GameAnswerRepository()

        // when
        sut.get()
        val actualGameAnswer = GameAnswer(word = answerDataSource.getCurrentAnswerToReturn!!.word().word())

        // then
        assertEquals(expectedGameAnswer, actualGameAnswer)
    }

    @Test
    fun `When get method invoked and answer data source throws an exception - expected exception is thrown`()
    {
        // given
        val expectedErrorMessage = "Answer not found."
        answerDataSource.getCurrentAnswerShouldFail = true

        // when
        val actualException = assertFailsWith<GameAnswerNotFoundRepositoryException>()
        {
            GameAnswerRepository().get()
        }

        // then
        assertEquals(expectedErrorMessage, actualException.message)
    }

    class MockWordLocalDataSource: WordLocalDataManageable
    {
        var excludingWords: List<Word> = listOf()
        var getShouldFail = false
        var wordToReturn: Word? = null

        override fun get(excludingWords: List<Word>): Word
        {
            if (getShouldFail) throw WordNotFoundLocalDataException(message = "Not found.")

            wordToReturn = Word(WordLocalDataSourceMock.mockWords.first())
            this.excludingWords = excludingWords

            return wordToReturn!!
        }

        override fun getAll(): List<Word> = listOf(wordToReturn!!)
    }

    class MockAnswerLocalDataSource: AnswerLocalDataManageable
    {
        var getCurrentAnswerToReturn: Answer? = null
        var getCurrentAnswerShouldFail: Boolean = false

        var getPreviousAnswersToReturn = listOf<Answer>()

        var putNewAnswerToReturn: Answer? = null
        var putNewAnswerShouldFail: Boolean = false

        var updatedAnswerToReturn: Answer? = null
        var updateAnswerShouldFail: Boolean = false

        override fun getCurrent(): Answer
        {
            if (getCurrentAnswerShouldFail) throw AnswerNotFoundLocalDataException(message = "Answer not found.")

            getCurrentAnswerToReturn = Answer(word = Word(word = "SLAYS"), isCurrent = true)

            return getCurrentAnswerToReturn!!
        }

        override fun getPrevious(): List<Answer> = getPreviousAnswersToReturn

        override suspend fun put(newAnswer: Answer): Answer
        {
            if (putNewAnswerShouldFail) throw AnswerPutFailedLocalDataException(message = "New Answer not saved.")

            putNewAnswerToReturn = newAnswer

            return putNewAnswerToReturn!!
        }

        override fun update(existingAnswer: Answer): Answer
        {
            if (updateAnswerShouldFail) throw AnswerUpdateFailedLocalDataException(message = "Existing Answer not updated.")

            updatedAnswerToReturn = existingAnswer

            return updatedAnswerToReturn!!
        }
    }
}