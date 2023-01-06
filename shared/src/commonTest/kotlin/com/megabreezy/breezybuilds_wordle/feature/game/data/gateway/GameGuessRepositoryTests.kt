package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway

import com.megabreezy.breezybuilds_wordle.core.data.source.answer.mock.AnswerLocalDataSourceMock
import com.megabreezy.breezybuilds_wordle.core.data.source.guess.GuessClearFailedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.guess.GuessLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.guess.GuessSaveFailedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.domain.model.Guess
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessCreateFailedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameGuess
import com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case.GameUseCase
import com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case.getGameBoard
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class GameGuessRepositoryTests
{
    private lateinit var guessLocalDataSource: MockGuessLocalDataSource

    @BeforeTest
    fun setUp()
    {
        guessLocalDataSource = MockGuessLocalDataSource()

        startKoin()
        {
            modules(
                CoreKoinModule(scenarios = listOf(Scenario.ANSWER_SAVED)).mockModule(),
                GameKoinModule().module(),
                module { single<GuessLocalDataManageable> { guessLocalDataSource } }
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `when the create method is invoked - the guess local data source save method is invoked - passing in expected newGuess`()
    {
        // given
        val expectedGuess = Guess(Word("FLAKE"))
        val gameBoard = GameUseCase().getGameBoard()
        gameBoard.activeRow()?.forEachIndexed()
        { index, tile ->
            tile.setLetter(newLetter = expectedGuess.word().toString()[index])
        }
        val sut = GameGuessRepository()

        // when
        sut.create()

        // then
        assertEquals(expectedGuess.word().toString(), guessLocalDataSource.newGuess)
    }

    @Test
    fun `when attempting to create a GameGuess and data source throws an exception - expected exception is thrown`()
    {
        // given
        val sut = GameGuessRepository()
        val expectedErrorMessage = "Failed to save new Guess."
        guessLocalDataSource.saveShouldFail = true

        // when
        val actualException = assertFailsWith<GameGuessCreateFailedRepositoryException>() { sut.create() }

        // then
        assertEquals(expectedErrorMessage, actualException.message)
    }

    @Test
    fun `when the getAll method is invoked - the guess local data source getAll method is invoked`()
    {
        // given
        val sut = GameGuessRepository()

        // when
        sut.getAll()

        // then
        assertNotNull(guessLocalDataSource.getAllGuesses)
    }

    @Test
    fun `when getting all guesses and data source returns a list of results - expected GameGuess list is returned`()
    {
        // given
        val sut = GameGuessRepository()
        val expectedGameGuessList = mutableListOf<GameGuess>()

        // when
        val actualList = sut.getAll()
        guessLocalDataSource.getAllGuesses?.forEach { expectedGameGuessList.add(GameGuess(word = it.word().toString())) }

        // then
        assertEquals(expectedGameGuessList.toList(), actualList)
    }

    @Test
    fun `when the clear method is invoked - the guess local data source clear method is invoked`()
    {
        // given
        val sut = GameGuessRepository()

        // when
        sut.clear()

        // then
        assertTrue(guessLocalDataSource.clearDidInvoke)
    }

    @Test
    fun `when clearing guesses and data source throws an exception - expected exception is thrown`()
    {
        // given
        val expectedExceptionMessage = "Failed to clear all Guesses."
        val sut = GameGuessRepository()
        guessLocalDataSource.clearShouldFail = true

        // when
        val actualException = assertFailsWith<GameGuessCreateFailedRepositoryException> { sut.clear() }

        // then
        assertEquals(expectedExceptionMessage, actualException.message)
    }

    class MockGuessLocalDataSource: GuessLocalDataManageable
    {
        var clearDidInvoke = false
        var clearShouldFail = false
        var getAllGuesses: List<Guess>? = null
        var newGuess: String? = null
        var saveShouldFail = false

        override fun getAll(): List<Guess>
        {
            getAllGuesses = listOf(
                Guess(word = Word("SNIPE")),
                Guess(word = Word("STEAK"))
            )

            return getAllGuesses!!
        }

        override fun save(newGuess: String): Guess
        {
            if (saveShouldFail) throw GuessSaveFailedLocalDataException(message = "Failed to save new Guess.")

            this.newGuess = newGuess

            return Guess(word = Word(newGuess))
        }

        override fun clear()
        {
            if (clearShouldFail) throw GuessClearFailedLocalDataException(message = "Failed to clear all Guesses.")

            clearDidInvoke = true
        }
    }
}