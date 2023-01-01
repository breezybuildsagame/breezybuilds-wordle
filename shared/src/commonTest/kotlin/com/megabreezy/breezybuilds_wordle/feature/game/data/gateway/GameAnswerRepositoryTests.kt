package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway

import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.word.mock.WordLocalDataSourceMock
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class GameAnswerRepositoryTests
{
    lateinit var dataSource: MockLocalDataSource

    @BeforeTest
    fun setUp()
    {
        dataSource = MockLocalDataSource()

        startKoin()
        {
            modules(
                CoreKoinModule(scenarios = listOf(Scenario.WORD_FOUND)).module(),
                GameKoinModule().module(),
                module { single<WordLocalDataManageable> { dataSource } }
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `when create method is invoked - local data manageable get method is invoked`()
    {
        // given
        val sut = GameAnswerRepository()

        // when
        sut.create()

        // then
        assertNotNull(dataSource.wordToReturn)
    }

    @Test
    fun `when creating answer and local data manageable returns a word - expected GameAnswer is returned`()
    {
        // given
        val sut = GameAnswerRepository()

        // when
        val actualWord = sut.create()
        val expectedWord = GameAnswer(word = dataSource.wordToReturn!!.word())

        // then
        assertEquals(expectedWord, actualWord)
    }

    @Test
    fun `when creating answer and local data manageable throws an exception - expected exception is thrown`()
    {
        // given
        val expectedExceptionMessage = "Not found."
        val sut = GameAnswerRepository()
        dataSource.getShouldFail = true

        // when
        val actualException = assertFailsWith<GameAnswerNotFoundRepositoryException>
        {
            sut.create()
        }

        // then
        assertEquals(expectedExceptionMessage, actualException.message)
    }

    class MockLocalDataSource: WordLocalDataManageable
    {
        var getShouldFail = false
        var wordToReturn: Word? = null

        override fun get(): Word
        {
            if (getShouldFail) throw WordNotFoundLocalDataException(message = "Not found.")

            wordToReturn = Word(WordLocalDataSourceMock.mockWords.first())

            return wordToReturn!!
        }
    }
}