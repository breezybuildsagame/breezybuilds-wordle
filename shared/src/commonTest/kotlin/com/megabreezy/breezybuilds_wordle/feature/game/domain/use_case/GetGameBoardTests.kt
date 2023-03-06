package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class GetGameBoardTests: KoinComponent
{
    private lateinit var answerLocalDataSource: MockAnswerLocalDataSource

    @BeforeTest
    fun setUp()
    {
        answerLocalDataSource = MockAnswerLocalDataSource()

        startKoin()
        {
            modules(
                CoreKoinModule().mockModule(),
                GameKoinModule().module(),
                module { single<AnswerLocalDataManageable> { answerLocalDataSource } }
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `When use case is invoked - expected GameBoard is returned`()
    {
        // given
        val expectedGameBoard: GameBoard by inject()
        val expectedRows = listOf(
            listOf(GameBoard.Tile(letter = 'A'), GameBoard.Tile(letter = 'Z'))
        )

        // when
        val actualGameBoard = runBlocking { GameUseCase().getGameBoard() }
        expectedGameBoard.setRows(newRows = expectedRows)

        // then
        assertEquals(expectedGameBoard.rows(), actualGameBoard.rows())
    }

    @Test
    fun `when use case is invoked - rows count is equal to six`()
    {
        // when
        val actualGameBoard = runBlocking { GameUseCase().getGameBoard() }

        // then
        assertEquals(6, actualGameBoard.rows().count())
    }

    @Test
    fun `when use case is invoked - getGameAnswer use case is invoked`()
    {
        // when
        runBlocking { GameUseCase().getGameBoard() }

        // then
        assertTrue(answerLocalDataSource.getDidInvoke)
    }

    @Test
    fun `when use case is invoked - each row contains tile count matching current answer`()
    {
        // given
        val expectedRowTileCount = answerLocalDataSource.expectedAnswer.word().toString().count()

        // when
        val actualGameBoard = runBlocking { GameUseCase().getGameBoard() }

        // then
        assertEquals(expectedRowTileCount, actualGameBoard.rows().first().size)
    }

    class MockAnswerLocalDataSource: AnswerLocalDataManageable
    {
        var getDidInvoke = false
        val expectedAnswer = Answer(word = Word(word = "HELLO"))

        override fun getCurrent(): Answer
        {
            getDidInvoke = true
            return expectedAnswer
        }
        override fun getPrevious(): List<Answer> = listOf()
        override suspend fun insert(newAnswer: Answer) = newAnswer
        override suspend fun update(existingAnswer: Answer, updatedAnswer: Answer) = updatedAnswer
    }
}