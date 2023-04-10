package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.mock.AnswerLocalDataSourceCommonMock
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.mock.GameGuessRepositoryCommonMock
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameGuess
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard
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
    private lateinit var answerLocalDataSource: AnswerLocalDataSourceCommonMock
    private lateinit var guessRepository: GameGuessRepositoryCommonMock

    @BeforeTest
    fun setUp()
    {
        answerLocalDataSource = AnswerLocalDataSourceCommonMock()
        guessRepository = GameGuessRepositoryCommonMock()

        startKoin()
        {
            modules(
                CoreKoinModule().mockModule(),
                GameKoinModule().module(),
                module()
                {
                    single<AnswerLocalDataManageable> { answerLocalDataSource }
                    single<GameGuessGateway> { guessRepository }
                }
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
        assertNotNull(answerLocalDataSource.getCurrentAnswerToReturn)
    }

    @Test
    fun `when use case is invoked - each row contains tile count matching current answer`()
    {
        // given
        val expectedRowTileCount = answerLocalDataSource.getCurrent().word().toString().count()

        // when
        val actualGameBoard = runBlocking { GameUseCase().getGameBoard() }

        // then
        assertEquals(expectedRowTileCount, actualGameBoard.rows().first().size)
    }

    @Test
    fun `When use case invoked reloadIfNecessary set true and current game answer found and guesses found - GameBoard matches in progress state`()
    {
        // given
        answerLocalDataSource.getCurrentAnswerToReturn = Answer(word = Word("SLAPS"))
        guessRepository.getAllGuessesToReturn = listOf(
            GameGuess(word = "STOPS"),
            GameGuess(word = "TARPS")
        )
        val expectedGameBoardRows = listOf(
            listOf(GameBoard.Tile(letter = 'S', state = GameBoard.Tile.State.CORRECT),
                   GameBoard.Tile(letter = 'T', state = GameBoard.Tile.State.INCORRECT),
                   GameBoard.Tile(letter = 'O', state = GameBoard.Tile.State.INCORRECT),
                   GameBoard.Tile(letter = 'P', state = GameBoard.Tile.State.CORRECT),
                   GameBoard.Tile(letter = 'S', state = GameBoard.Tile.State.CORRECT)),
            listOf(GameBoard.Tile(letter = 'T', state = GameBoard.Tile.State.INCORRECT),
                   GameBoard.Tile(letter = 'A', state = GameBoard.Tile.State.CLOSE),
                   GameBoard.Tile(letter = 'R', state = GameBoard.Tile.State.INCORRECT),
                   GameBoard.Tile(letter = 'P', state = GameBoard.Tile.State.CORRECT),
                   GameBoard.Tile(letter = 'S', state = GameBoard.Tile.State.CORRECT)),
            listOf(GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile()),
            listOf(GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile()),
            listOf(GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile()),
            listOf(GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile())
        )
        val expectedActiveGameRow = expectedGameBoardRows[2]

        // when
        val actualGameBoard = runBlocking { GameUseCase().getGameBoard(resetIfNecessary = true, reloadIfNecessary = true) }

        // then
        assertEquals(expectedGameBoardRows, actualGameBoard.rows())
        assertEquals(expectedActiveGameRow, actualGameBoard.activeRow())
    }

    @Test
    fun `When use case invoked reloadIfNecessary set true and current game answer found and guesses found - GameKeyboard matches in progress state`()
    {
        // given
        val answer = Answer(word = Word("SLAPS"))
        answerLocalDataSource.getCurrentAnswerToReturn = answer
        guessRepository.getAllGuessesToReturn = listOf(
            GameGuess(word = "STOPS"),
            GameGuess(word = "TARPS")
        )

        // when
        runBlocking { GameUseCase().getGameBoard(resetIfNecessary = true, reloadIfNecessary = true) }
        val keys = GameUseCase().getGameKeyboard().rows().flatten().filter { it.backgroundColor() != GameKeyboard.Key.BackgroundColor.DEFAULT }

        // then
        assertEquals(6, keys.count()) // 6 = unique letters guessed (S, T, O, P, A, R)
        for (key in keys)
        {
            when (key.letter())
            {
                'S' -> assertEquals(GameKeyboard.Key.BackgroundColor.CORRECT, key.backgroundColor())
                'T' -> assertEquals(GameKeyboard.Key.BackgroundColor.NOT_FOUND, key.backgroundColor())
                'O' -> assertEquals(GameKeyboard.Key.BackgroundColor.NOT_FOUND, key.backgroundColor())
                'P' -> assertEquals(GameKeyboard.Key.BackgroundColor.CORRECT, key.backgroundColor())
                'A' -> assertEquals(GameKeyboard.Key.BackgroundColor.NEARBY, key.backgroundColor())
                'R' -> assertEquals(GameKeyboard.Key.BackgroundColor.NOT_FOUND, key.backgroundColor())
            }
        }
    }

    @Test
    fun `When use case invoked resetIfNecessary set true and all gameBoard row tiles are unhidden - GameBoard is reset`()
    {
        // given
        val expectedGameboardRow = listOf(
            GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile()
        )
        val filledGameboardRow = listOf(
            GameBoard.Tile(letter = 'B', state = GameBoard.Tile.State.INCORRECT),
            GameBoard.Tile(letter = 'B', state = GameBoard.Tile.State.INCORRECT),
            GameBoard.Tile(letter = 'B', state = GameBoard.Tile.State.INCORRECT),
            GameBoard.Tile(letter = 'B', state = GameBoard.Tile.State.INCORRECT),
            GameBoard.Tile(letter = 'B', state = GameBoard.Tile.State.INCORRECT)
        )
        val answer = Answer(word = Word("SLAPS"))
        answerLocalDataSource.getCurrentAnswerToReturn = answer
        guessRepository.getAllGuessesToReturn = listOf()
        val gameBoard: GameBoard by inject()
        gameBoard.setRows(
            newRows = listOf(
                filledGameboardRow, filledGameboardRow, filledGameboardRow,
                filledGameboardRow, filledGameboardRow, filledGameboardRow
            )
        )

        // when
        runBlocking { GameUseCase().getGameBoard(resetIfNecessary = true) }

        // then
        gameBoard.rows().forEachIndexed()
        { index, row ->
            assertEquals(expectedGameboardRow, gameBoard.rows()[index])
        }
    }

    @Test
    fun `When use case invoked resetIfNecessary set true and one gameboard row is correct - GameBoard is reset`()
    {
        // given

        // when

        // then
        assertTrue(false)
    }
}