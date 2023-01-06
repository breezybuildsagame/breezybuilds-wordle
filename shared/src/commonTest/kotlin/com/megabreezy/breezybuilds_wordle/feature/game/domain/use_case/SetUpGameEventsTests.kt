package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessCreateFailedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.*
import com.megabreezy.breezybuilds_wordle.feature.game.presentation.GameSceneHandleable
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class SetUpGameEventsTests: KoinComponent
{
    private lateinit var answerRepository: MockGameAnswerRepository
    private lateinit var guessRepository: MockGameGuessRepository
    private lateinit var sceneHandler: MockSceneHandler

    private val announcement: Announcement by inject()
    private val gameBoard: GameBoard by inject()
    private val keyboard: GameKeyboard by inject()

    @BeforeTest
    fun setUp()
    {
        answerRepository = MockGameAnswerRepository()
        guessRepository = MockGameGuessRepository()
        sceneHandler = MockSceneHandler()

        startKoin()
        {
            modules(
                CoreKoinModule(scenarios = listOf(Scenario.WORD_FOUND, Scenario.ANSWER_SAVED)).mockModule(),
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
        val announcement: Announcement by inject()
        announcement.setMessage(newMessage = "My Urgent Message!")

        // when
        GameUseCase().setUpGameEvents()

        // then
        assertNull(announcement.message())
    }

    @Test
    fun `when use case is invoked and letter Key is clicked - GameBoard Tile is updated to expected letter`()
    {
        // given
        val expectedLetter = 'B'

        // when
        GameUseCase().setUpGameEvents()
        for (row in keyboard.rows()) { for (key in row) { if (key.letter() == expectedLetter) key.click() } }

        // then
        assertEquals(expectedLetter, gameBoard.rows().first().first().letter())
    }

    @Test
    fun `when use case is invoked and letter Key is clicked - handler onRevealNextTile method is invoked`()
    {
        // when
        GameUseCase().setUpGameEvents(sceneHandler = sceneHandler)
        for (row in keyboard.rows()) { for (key in row) { if (key.letter() == 'Z') key.click() } }

        // then
        assertTrue(sceneHandler.onRevealNextTileDidInvoke)
    }

    @Test
    fun `when use case is invoked and letter Key is clicked and GameBoard activeRow is unchanged - handler onRevealNextTile method is not invoked`()
    {
        // given
        GameUseCase().setUpGameEvents(sceneHandler = sceneHandler)
        val cRow = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letter() == 'C' } != null }
        val cKey = cRow?.first { it.letter() == 'C' }
        for (row in gameBoard.rows()) { for (tile in row) { cKey?.click() } }
        sceneHandler.onRevealNextTileDidInvoke = false

        // when
        cKey?.click()

        // then
        assertFalse(sceneHandler.onRevealNextTileDidInvoke)
    }

    @Test
    fun `when use case is invoked and GameBoard activeRow contains letters and backspace Key is clicked - GameBoard activeRow is updated to remove latest letter`()
    {
        // given
        GameUseCase().setUpGameEvents(sceneHandler = sceneHandler)
        val cRow = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letter() == 'C' } != null }
        val cKey = cRow?.first { it.letter() == 'C' }
        val backSpaceRow = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letters() == "BACKSPACE"} != null }
        val backSpaceKey = backSpaceRow?.first { it.letters() == "BACKSPACE" }
        cKey?.click()
        sceneHandler.onRevealNextTileDidInvoke = false

        // when
        backSpaceKey?.click()

        // then
        assertNull(gameBoard.activeRow()?.firstOrNull { tile -> tile.letter() != null })
        assertTrue(sceneHandler.onRevealNextTileDidInvoke)
    }

    @Test
    fun `when use case is invoked and GameBoard activeRow contains no letters and backspace Key is clicked - activeRow remains empty`()
    {
        // given
        GameUseCase().setUpGameEvents(sceneHandler = sceneHandler)
        val backSpaceRow = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letters() == "BACKSPACE"} != null }
        val backSpaceKey = backSpaceRow?.first { it.letters() == "BACKSPACE" }

        // when
        backSpaceKey?.click()

        // then
        assertNull(gameBoard.activeRow()?.firstOrNull { tile -> tile.letter() != null })
        assertFalse(sceneHandler.onRevealNextTileDidInvoke)
    }

    @Test
    fun `when use case is invoked and enter Key is clicked - guessWord use case is invoked`()
    {
        // given
        GameUseCase().setUpGameEvents(sceneHandler = sceneHandler)
        val cRow = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letter() == 'C' } != null }
        val cKey = cRow?.first { it.letter() == 'C' }
        val enterRow = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letters() == "ENTER" } != null }
        val enterKey = enterRow?.first { it.letters() == "ENTER" }
        for (row in gameBoard.rows()) { for (tile in row) { cKey?.click() } }

        // when
        try { enterKey?.click() } catch(_ : Throwable) { }

        // then
        assertNotNull(guessRepository.guessToReturn)
        assertNotNull(answerRepository.gameAnswer)
    }

    @Test
    fun `when use case is invoked and enter Key is clicked and GameGuess is invalid - no exception is thrown`()
    {
        // given
        GameUseCase().setUpGameEvents(sceneHandler = sceneHandler)
        val cRow = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letter() == 'C' } != null }
        val cKey = cRow?.first { it.letter() == 'C' }
        val enterRow = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letters() == "ENTER" } != null }
        val enterKey = enterRow?.first { it.letters() == "ENTER" }
        cKey?.click()
        guessRepository.guessIsInvalid = true

        // when
        enterKey?.click()

        // then
        assertNotNull(guessRepository.guessToReturn)
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess contains an incorrect letter - GameKeyboard Key background colors are updated accordingly`()
    {
        // given
        GameUseCase().setUpGameEvents(sceneHandler = sceneHandler)
        val enterRow = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letters() == "ENTER" } != null }
        val enterKey = enterRow?.first { it.letters() == "ENTER" }

        val pRow = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letters() == "P" } != null }
        val pKey = pRow?.first { it.letters() == "P" }
        val lRow = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letters() == "L" } != null }
        val lKey = lRow?.first { it.letters() == "L" }
        val aRow = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letters() == "A" } != null }
        val aKey = aRow?.first { it.letters() == "A" }
        val yRow = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letters() == "Y" } != null }
        val yKey = yRow?.first { it.letters() == "Y" }
        val sRow = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letters() == "S" } != null }
        val sKey = sRow?.first { it.letters() == "S" }

        // when
        pKey?.click()
        lKey?.click()
        aKey?.click()
        yKey?.click()
        sKey?.click()
        enterKey?.click()

        // then
        assertEquals(GameKeyboard.Key.BackgroundColor.NOT_FOUND, pKey?.backgroundColor())
        assertEquals(GameKeyboard.Key.BackgroundColor.NOT_FOUND, lKey?.backgroundColor())
        assertEquals(GameKeyboard.Key.BackgroundColor.NOT_FOUND, aKey?.backgroundColor())
        assertEquals(GameKeyboard.Key.BackgroundColor.NOT_FOUND, yKey?.backgroundColor())
        assertNotEquals(GameKeyboard.Key.BackgroundColor.NOT_FOUND, sKey?.backgroundColor())
        for (row in keyboard.rows())
        {
            for (key in row)
            {
                if (key != pKey && key != lKey && key != aKey && key != yKey && key != sKey && key.letter() != null)
                {
                    assertEquals(GameKeyboard.Key.BackgroundColor.DEFAULT, key.backgroundColor())
                }
            }
        }
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess contains a close letter - GameKeyboard Key background colors are updated accordingly`()
    {
        // given

        // when
        GameUseCase().setUpGameEvents()

        // then
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess contains a correct letter - GameKeyboard Key background colors are updated accordingly`()
    {
        // given

        // when
        GameUseCase().setUpGameEvents()

        // then
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is not found in words list - expected announcement is set`()
    {
        // given

        // when
        GameUseCase().setUpGameEvents()

        // then
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is incorrect - expected announcement is set`()
    {
        // given

        // when
        GameUseCase().setUpGameEvents()

        // then
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is incorrect - GameBoard setNewActiveRow method is invoked`()
    {
        // given

        // when
        GameUseCase().setUpGameEvents()

        // then
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is incorrect - handler onRoundCompleted method is invoked`()
    {
        // given

        // when
        GameUseCase().setUpGameEvents()

        // then
    }

    @Test
    fun `when GameBoard setNewActiveRow throws an exception - expected announcement is set`()
    {
        // given

        // when
        GameUseCase().setUpGameEvents()

        // then
    }

    @Test
    fun `when GameBoard setNewActiveRow throws an exception - handler onGameOver method is invoked`()
    {
        // given

        // when
        GameUseCase().setUpGameEvents()

        // then
    }

    class MockSceneHandler: GameSceneHandleable
    {
        var onGameOverDidInvoke = false
        var onGameStartedDidInvoke = false
        var onGuessingWordDidInvoke = false
        var onRevealNextTileDidInvoke = false
        var onRoundCompletedDidInvoke = false
        var onStartingGameDidInvoke = false

        override fun onGameOver() { onGameOverDidInvoke = true }
        override fun onGameStarted() { onGameStartedDidInvoke = true }
        override fun onGuessingWord() { onGuessingWordDidInvoke = true }
        override fun onRevealNextTile() { onRevealNextTileDidInvoke = true }
        override fun onRoundCompleted() { onRoundCompletedDidInvoke = true }
        override fun onStartingGame() { onStartingGameDidInvoke = true }
    }

    class MockGameGuessRepository: GameGuessGateway
    {
        var guessIsInvalid = false
        var guessNotFound = false
        var guessToReturn: GameGuess? = null

        override fun create(): GameGuess
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
        var getShouldFail = false
        var guessMatchesAnswer = false

        override fun create(): GameAnswer
        {
            createdGameAnswer = if (guessMatchesAnswer) GameAnswer(word = "PLAYS") else GameAnswer(word = "TESTS")

            return createdGameAnswer!!
        }

        override fun get(): GameAnswer = createdGameAnswer?.let()
        {
            if (getShouldFail) throw GameAnswerNotFoundRepositoryException("Answer not found.")

            gameAnswer = createdGameAnswer

            return gameAnswer!!
        }
            ?: create()
    }
}