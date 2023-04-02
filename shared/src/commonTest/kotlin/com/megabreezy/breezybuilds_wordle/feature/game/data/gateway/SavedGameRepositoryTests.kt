package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway

import com.megabreezy.breezybuilds_wordle.core.data.source.answer.mock.AnswerLocalDataSourceCommonMock
import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.mock.CompletedGameLocalDataSourceCommonMock
import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.mock.GameAnswerRepositoryCommonMock
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.SavedGameCreateFailedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class SavedGameRepositoryTests
{
    lateinit var completedGameLocalDataSource: CompletedGameLocalDataSourceCommonMock
    lateinit var gameAnswerRepository: GameAnswerRepositoryCommonMock

    @BeforeTest
    fun setUp()
    {
        completedGameLocalDataSource = CompletedGameLocalDataSourceCommonMock()
        gameAnswerRepository = GameAnswerRepositoryCommonMock()

        startKoin()
        {
            modules(
                CoreKoinModule().mockModule(),
                GameKoinModule().module(),
                module { single<CompletedGameLocalDataManageable> { completedGameLocalDataSource } },
                module { single<GameAnswerGateway> { gameAnswerRepository } }
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `When create method invoked - injected GameAnswerGateways get method is invoked`()
    {
        // given
        val sut = SavedGameRepository()

        // when
        runBlocking { sut.create() }

        // then
        assertNotNull(gameAnswerRepository.createdGameAnswer)
    }

    @Test
    fun `When create method invoked and GameAnswerGateway throws an exception - expected exception is thrown`()
    {
        // given
        gameAnswerRepository.getShouldFail = true
        val expectedExceptionMessage = gameAnswerRepository.getExceptionMessage
        val sut = SavedGameRepository()

        // when
        val actualException = assertFailsWith<SavedGameCreateFailedRepositoryException>
        {
            runBlocking { sut.create() }
        }

        // then
        assertEquals(expectedExceptionMessage, actualException.message)
    }

    @Test
    fun `When create method invoked - injected CompletedGameLocalDataManageable put method is invoked passing in expected CompletedGame`()
    {
        // given
        /**
        val expectedCompletedGame = CompletedGame(
            answer = Answer(word = Word("STRAYS"), playerGuessedCorrectly = true),
            date = Clock.System.now().epochSeconds,
            playerGuesses = listOf(
                Guess(word = Word("FLIGHT")),
                Guess(word = Word("SLAYS")),
                Guess(word = Word("STRAYS"))
            )
        )
        val sut = SavedGameRepository()

        // when
        val actualCompletedGame = runBlocking { sut.create() }

        // then
        assertNotNull(actualCompletedGame)
        assertEquals(expectedCompletedGame, localDataSource.putGames.first())
        **/
    }

    @Test
    fun `When create method invoked and data source throws an exception - expected exception is thrown`()
    {

    }
}