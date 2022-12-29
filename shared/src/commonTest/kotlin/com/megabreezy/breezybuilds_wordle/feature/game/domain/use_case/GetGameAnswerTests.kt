package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class GetGameAnswerTests
{
    lateinit var repository: MockRepository

    @BeforeTest
    fun setUp()
    {
        repository = MockRepository()

        startKoin()
        {
            modules(
                CoreKoinModule().module(),
                GameKoinModule().module(),
                module { single<GameAnswerGateway> { repository } }
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `hen use case invoked - gateway get method is invoked`()
    {
        // when
        GameUseCase().getGameAnswer()

        // then
        assertNotNull(repository.gameAnswerToReturn)
    }

    @Test
    fun `hen use case invoked and gateway returns a GameAnswer - expected GameAnswer is returned`()
    {
        // given
        val expectedGameAnswer = GameAnswer(word = "AWESOME")

        // when
        val actualGameAnswer = GameUseCase().getGameAnswer()

        // then
        assertEquals(expectedGameAnswer, actualGameAnswer)
    }

    @Test
    fun `hen use case invoked and gateway throws and exception - expected exception is thrown`()
    {
        // given
        val expectedExceptionMessage = "Not found."
        repository.getShouldFail = true

        // when
        val actualException = assertFailsWith<GameUseCase.GetGameAnswerFailedException>()
        {
            GameUseCase().getGameAnswer()
        }

        // then
        assertEquals(expectedExceptionMessage, actualException.message)
    }

    class MockRepository: GameAnswerGateway
    {
        var gameAnswerToReturn: GameAnswer? = null
        var getShouldFail = false

        override fun get(): GameAnswer
        {
            if (getShouldFail) throw GameAnswerNotFoundRepositoryException("Not found.")

            gameAnswerToReturn = GameAnswer(word = "AWESOME")

            return gameAnswerToReturn!!
        }
    }
}