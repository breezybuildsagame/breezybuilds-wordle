package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.*

class SetUpGameEventsTests: KoinComponent
{
    @BeforeTest
    fun setUp()
    {
        startKoin()
        {
            modules(
                CoreKoinModule(scenarios = listOf(Scenario.WORD_FOUND, Scenario.ANSWER_SAVED)).mockModule(),
                GameKoinModule().module()
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `when use case is invoked - GameKeyboard reset method is invoked`()
    {
        // given
        val keyboard: GameKeyboard by inject()
        keyboard.rows().first().forEach { it.setBackgroundColor(GameKeyboard.Key.BackgroundColor.NOT_FOUND) }

        // when
        GameUseCase().setUpGameEvents()

        // then
        keyboard.rows().first().forEach { assertEquals(GameKeyboard.Key.BackgroundColor.DEFAULT, it.backgroundColor()) }
    }

    @Test
    fun `when use case is invoked - GameBoard reset method is invoked`()
    {
        // given
        val gameBoard = GameUseCase().getGameBoard()
        for (tile in gameBoard.rows().first())
        {
            tile.setLetter(newLetter = 'T')
            tile.setState(newState = GameBoard.Tile.State.INCORRECT)
        }
        gameBoard.setNewActiveRow()

        // when
        GameUseCase().setUpGameEvents()

        // then
        for (row in gameBoard.rows())
        {
            for (tile in row)
            {
                assertEquals(GameBoard.Tile.State.HIDDEN, tile.state())
                assertNull(tile.letter())
            }
        }
        assertEquals(gameBoard.rows().first(), gameBoard.activeRow())
    }

    @Test
    fun `when use case is invoked - Announcement message method returns null value`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when use case is invoked and letter Key is clicked - GameBoard Tile is updated to expected letter`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when use case is invoked and letter Key is clicked - handler onRevealNextTile method is invoked`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when use case is invoked and letter Key is clicked and GameBoard activeRow is unchanged - handler onRevealNextTile method is not invoked`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when use case is invoked and GameBoard activeRow contains letters and backspace Key is clicked - GameBoard activeRow is updated to remove latest letter`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when use case is invoked and GameBoard activeRow contains no letters and backspace Key is clicked - activeRow remains empty`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when use case is invoked and enter Key is clicked - guessWord use case is invoked`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when use case is invoked and enter Key is clicked and GameGuess is invalid - no exception is thrown`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess contains an incorrect letter - GameKeyboard Key background colors are updated accordingly`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess contains a close letter - GameKeyboard Key background colors are updated accordingly`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess contains a correct letter - GameKeyboard Key background colors are updated accordingly`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is not found in words list - expected announcement is set`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is incorrect - expected announcement is set`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is incorrect - GameBoard setNewActiveRow method is invoked`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is incorrect - handler onRoundCompleted method is invoked`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when GameBoard setNewActiveRow throws an exception - expected announcement is set`()
    {
        // given

        // when

        // then
    }

    @Test
    fun `when GameBoard setNewActiveRow throws an exception - handler onGameOver method is invoked`()
    {
        // given

        // when

        // then
    }
}