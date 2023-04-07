package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway

import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.mock.CompletedGameLocalDataSourceCommonMock
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.CompletedGame
import com.megabreezy.breezybuilds_wordle.core.domain.model.Guess
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.mock.GameAnswerRepositoryCommonMock
import com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.mock.GameGuessRepositoryCommonMock
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.SavedGameCreateFailedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameGuess
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class SavedGameRepositoryTests
{
    lateinit var completedGameLocalDataSource: CompletedGameLocalDataSourceCommonMock
    lateinit var gameAnswerRepository: GameAnswerRepositoryCommonMock
    lateinit var gameGuessRepository: GameGuessRepositoryCommonMock

    @BeforeTest
    fun setUp()
    {
        completedGameLocalDataSource = CompletedGameLocalDataSourceCommonMock()
        gameAnswerRepository = GameAnswerRepositoryCommonMock()
        gameGuessRepository = GameGuessRepositoryCommonMock()

        startKoin()
        {
            modules(
                CoreKoinModule().mockModule(),
                GameKoinModule().module(),
                module { single<CompletedGameLocalDataManageable> { completedGameLocalDataSource } },
                module { single<GameAnswerGateway> { gameAnswerRepository } },
                module { single<GameGuessGateway> { gameGuessRepository } }
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
    fun `When create method invoked and GameAnswer found - injected GameGuessGateways getAll method is invoked`()
    {
        // given
        val sut = SavedGameRepository()

        // when
        runBlocking { sut.create() }

        // then
        assertNotNull(gameGuessRepository.getAllGuessesToReturn)
    }

    @Test
    fun `When create method invoked - injected CompletedGameLocalDataManageable put method is invoked passing in expected CompletedGame`()
    {
        // given
        val answer = Answer(word = Word("SLAPS"), playerGuessedCorrectly = true)
        val guesses = listOf(
            GameGuess(word = "SLAYS"),
            GameGuess(word = "SLAPS")
        )
        gameAnswerRepository.createdGameAnswer = GameAnswer(word = "${answer.word()}")
        gameGuessRepository.getAllGuessesToReturn = guesses
        val expectedCompletedGame = CompletedGame(
            answer = answer,
            date = Clock.System.now().epochSeconds,
            playerGuesses = guesses.map { Guess(word = Word(it.word())) }
        )
        val sut = SavedGameRepository()

        // when
        val actualCompletedGame = runBlocking { sut.create() }

        // then
        assertNotNull(actualCompletedGame)
        assertEquals(expectedCompletedGame, completedGameLocalDataSource.putGames.first())
    }

    @Test
    fun `When create method invoked and data source throws an exception - expected exception is thrown`()
    {
        // given
        completedGameLocalDataSource.putShouldFail = true
        val expectedExceptionMessage = completedGameLocalDataSource.putShouldFailErrorMessage
        val sut = SavedGameRepository()

        // when
        val actualException = assertFailsWith<SavedGameCreateFailedRepositoryException>
        {
            runBlocking { sut.create() }
        }

        // then
        assertEquals(expectedExceptionMessage, actualException.message)
    }
}