package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotCreatedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class GetGameAnswerTests
{
    private lateinit var repository: MockRepository

    @BeforeTest
    fun setUp()
    {
        repository = MockRepository()

        startKoin()
        {
            modules(
                CoreKoinModule().mockModule(),
                GameKoinModule().module(),
                module { single<GameAnswerGateway> { repository } }
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `when use case invoked - gateway get method is invoked`()
    {
        // when
        runBlocking { GameUseCase().getGameAnswer() }

        // then
        assertNotNull(repository.gameAnswerToReturn)
    }

    @Test
    fun `when use case invoked and gateway returns a GameAnswer - expected GameAnswer is returned`()
    {
        // given
        val expectedGameAnswer = GameAnswer(word = "AWESOME")

        // when
        val actualGameAnswer = runBlocking { GameUseCase().getGameAnswer() }

        // then
        assertEquals(expectedGameAnswer, actualGameAnswer)
    }

    @Test
    fun `When gateway get method throws an exception - gateway create method is invoked`()
    {
        // given
        repository.getShouldFail = true
        val expectedGameAnswer = GameAnswer(word = "AWESOME")

        // when
        val actualGameAnswer = runBlocking { GameUseCase().getGameAnswer() }

        // then
        assertEquals(expectedGameAnswer, actualGameAnswer)
    }

    @Test
    fun `when gateway create method throws an exception - expected exception is thrown`()
    {
        // given
        val expectedExceptionMessage = "Not found."
        repository.getShouldFail = true
        repository.createShouldFail = true

        // when
        val actualException = assertFailsWith<GameUseCase.GetGameAnswerFailedException>()
        {
            runBlocking { GameUseCase().getGameAnswer() }
        }

        // then
        assertEquals(expectedExceptionMessage, actualException.message)
    }

    class MockRepository: GameAnswerGateway
    {
        var gameAnswerToReturn: GameAnswer? = null
        var createShouldFail = false
        var getShouldFail = false

        override suspend fun create(): GameAnswer
        {
            if (createShouldFail) throw GameAnswerNotCreatedRepositoryException("Not found.")

            gameAnswerToReturn = GameAnswer(word = "AWESOME")

            return gameAnswerToReturn!!
        }

        override fun get(): GameAnswer
        {
            if (getShouldFail) throw GameAnswerNotFoundRepositoryException("Not found.")

            gameAnswerToReturn = GameAnswer(word = "AWESOME")

            return gameAnswerToReturn!!
        }
    }
}