package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.mock.GameAnswerRepositoryCommonMock
import com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.mock.GameGuessRepositoryCommonMock
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FinalizeActiveGameBoardRowTests
{
    private lateinit var answerRepository: GameAnswerRepositoryCommonMock
    private lateinit var guessRepository: GameGuessRepositoryCommonMock

    @BeforeTest
    fun setUp()
    {
        answerRepository = GameAnswerRepositoryCommonMock()
        guessRepository = GameGuessRepositoryCommonMock()

        startKoin()
        {
            modules(
                CoreKoinModule().mockModule(),
                GameKoinModule().module(),
                module()
                {
                    single<GameAnswerGateway> { answerRepository }
                    single<GameGuessGateway> { guessRepository }
                }
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `When use case invoked an active row contains REFER and answer is REVUE - GameBoard row matches expected updated state`()
    {
        // given
        answerRepository.getShouldFail = true
        answerRepository.createdGameAnswer = GameAnswer(word = "REVUE")

        val gameBoard = runBlocking { GameUseCase().getGameBoard() }
        gameBoard.setRows(newRows = listOf(
            listOf(
                GameBoard.Tile(letter = 'R'), GameBoard.Tile(letter = 'E'),
                GameBoard.Tile(letter = 'F'), GameBoard.Tile(letter = 'E'), GameBoard.Tile(letter = 'R')
            ),
            listOf(), listOf(), listOf(), listOf(), listOf()
        ))
        val expectedRow = listOf(
            GameBoard.Tile(letter = 'R', state = GameBoard.Tile.State.CORRECT),
            GameBoard.Tile(letter = 'E', state = GameBoard.Tile.State.CORRECT),
            GameBoard.Tile(letter = 'F', state = GameBoard.Tile.State.INCORRECT),
            GameBoard.Tile(letter = 'E', state = GameBoard.Tile.State.CLOSE),
            GameBoard.Tile(letter = 'R', state = GameBoard.Tile.State.INCORRECT)
        )

        // when
        runBlocking { GameUseCase().finalizeActiveGameBoardRow() }

        // then
        gameBoard.rows().first().forEach { println("${it.letter()} : ${it.state()}") }
        assertEquals(expectedRow, gameBoard.rows().first())
    }
}