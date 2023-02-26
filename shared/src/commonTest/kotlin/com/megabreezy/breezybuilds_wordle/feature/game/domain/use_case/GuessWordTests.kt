package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.*
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameGuess
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class GuessWordTests
{
    private lateinit var answerRepository: MockGameAnswerRepository
    private lateinit var guessRepository: MockGameGuessRepository

    @BeforeTest
    fun setUp()
    {
        answerRepository = MockGameAnswerRepository()
        guessRepository = MockGameGuessRepository()

        startKoin()
        {
            modules(
                CoreKoinModule(scenarios = listOf(Scenario.ANSWER_SAVED)).mockModule(),
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
    fun `when use case invoked - GameGuessGateway create method is invoked`()
    {
        // given
        answerRepository.guessMatchesAnswer = true
        runBlocking { GameUseCase().getGameBoard().activeRow()?.forEach { it.setLetter(newLetter = 'T') } }

        // when
        runBlocking { GameUseCase().guessWord() }

        // then
        assertNotNull(guessRepository.guessToReturn)
    }

    @Test
    fun `when invalid GameGuess is returned - expected exception is thrown`()
    {
        // given
        val expectedExceptionMessage = "Invalid guess: T"
        guessRepository.guessIsInvalid = true
        runBlocking()
        {
            GameUseCase().getGameBoard().activeRow()?.first()?.setLetter(newLetter = 'T')
            GameUseCase().getGameBoard().activeRow()?.forEach { it.setLetter(newLetter = 'T') }
        }

        // when
        val actualException = assertFailsWith<GameUseCase.GuessWordInvalidGuessException>()
        {
            runBlocking { GameUseCase().guessWord() }
        }

        // then
        assertEquals(expectedExceptionMessage, actualException.message)
    }

    @Test
    fun `when GameGuessGateway throws an exception - expected exception is thrown`()
    {
        // given
        val expectedExceptionMessage = "Not found in words list."
        guessRepository.guessNotFound = true
        runBlocking { GameUseCase().getGameBoard().activeRow()?.forEach { it.setLetter(newLetter = 'T') } }

        // when
        val actualException = assertFailsWith<GameUseCase.GuessWordFailedNotInWordsListException>()
        {
            runBlocking { GameUseCase().guessWord() }
        }

        // then
        assertEquals(expectedExceptionMessage, actualException.message)
    }

    @Test
    fun `when valid GameGuess is returned - the getGameAnswer use case is invoked`()
    {
        // given
        answerRepository.guessMatchesAnswer = true
        runBlocking { GameUseCase().getGameBoard().activeRow()?.forEach { it.setLetter(newLetter = 'T') } }

        // when
        runBlocking { GameUseCase().guessWord() }

        // then
        assertNotNull(answerRepository.gameAnswer)
    }

    @Test
    fun `when GameAnswerGateway throws an exception - expected exception is thrown`()
    {
        // given
        val expectedExceptionMessage = "Answer not found."
        answerRepository.getShouldFail = true
        answerRepository.createShouldFail = true

        // when
        val actualException = assertFailsWith<GameUseCase.GuessWordFailedException>
        {
            runBlocking { GameUseCase().guessWord() }
        }

        // then
        assertEquals(expectedExceptionMessage, actualException.message)
    }

    @Test
    fun `when GameAnswer is returned and GameGuess is invalid - expected exception is thrown`()
    {
        // given
        val expectedExceptionMessage = "Invalid guess: T"
        guessRepository.guessNotFound = true

        // when
        runBlocking { GameUseCase().getGameBoard().activeRow()?.first()?.setLetter(newLetter = 'T') }
        val actualException = assertFailsWith<GameUseCase.GuessWordInvalidGuessException>
        {
            runBlocking { GameUseCase().guessWord() }
        }

        // then
        assertEquals(expectedExceptionMessage, actualException.message)
    }

    @Test
    fun `when GameAnswer is returned and does not match GameGuess - expected exception is thrown`()
    {
        // given
        val expectedExceptionMessage = "Guess (PLAYS) does not match Answer (TESTS)."
        runBlocking { GameUseCase().getGameBoard().activeRow()?.forEach { it.setLetter(newLetter = 'T') } }

        // when
        val actualException = assertFailsWith<GameUseCase.GuessWordFailedMismatchException>()
        {
            runBlocking { GameUseCase().guessWord() }
        }

        // then
        assertEquals(expectedExceptionMessage, actualException.message)
    }

    @Test
    fun `when GameAnswer is returned and GameGuess matches - no exception is thrown`()
    {
        // given
        answerRepository.guessMatchesAnswer = true
        runBlocking { GameUseCase().getGameBoard().activeRow()?.forEach { it.setLetter(newLetter = 'T') } }

        // when
        runBlocking { GameUseCase().guessWord() }

        // then
        assertNotNull(answerRepository.gameAnswer)
        assertNotNull(guessRepository.guessToReturn)
    }

    class MockGameGuessRepository: GameGuessGateway
    {
        var guessIsInvalid = false
        var guessNotFound = false
        var guessToReturn: GameGuess? = null

        override suspend fun create(): GameGuess
        {
            if (guessNotFound) throw GameGuessCreateFailedRepositoryException("Not found in words list.")

            guessToReturn = if (guessIsInvalid) GameGuess(word = "T") else GameGuess(word = "PLAYS")

            return guessToReturn!!
        }

        override fun getAll(): List<GameGuess> = listOf()
        override fun clear() { }
    }

    class MockGameAnswerRepository: GameAnswerGateway
    {
         var createdGameAnswer: GameAnswer? = null
        var gameAnswer: GameAnswer? = null
        var createShouldFail = false
        var getShouldFail = false
        var guessMatchesAnswer = false

        override suspend fun create(): GameAnswer
        {
            if (createShouldFail) throw GameAnswerNotCreatedRepositoryException(message = "Answer not found.")

            createdGameAnswer = if (guessMatchesAnswer) GameAnswer(word = "PLAYS") else GameAnswer(word = "TESTS")

            return createdGameAnswer!!
        }

        override fun get(): GameAnswer = createdGameAnswer?.let()
        {
            if (getShouldFail) throw GameAnswerNotFoundRepositoryException("Answer not found.")

            gameAnswer = createdGameAnswer

            return gameAnswer!!
        }
        ?: runBlocking { create() }
    }
}