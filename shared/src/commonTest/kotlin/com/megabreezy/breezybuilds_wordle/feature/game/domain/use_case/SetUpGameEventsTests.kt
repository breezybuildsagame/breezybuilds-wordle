package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.*
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.*
import com.megabreezy.breezybuilds_wordle.feature.game.presentation.GameSceneHandleable
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import kotlinx.coroutines.runBlocking
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
    private lateinit var announcement: MockAnnouncement

    private val gameBoard: GameBoard by inject()
    private val keyboard: GameKeyboard by inject()

    @BeforeTest
    fun setUp()
    {
        answerRepository = MockGameAnswerRepository()
        guessRepository = MockGameGuessRepository()
        sceneHandler = MockSceneHandler()
        announcement = MockAnnouncement()

        startKoin()
        {
            modules(
                CoreKoinModule(scenarios = listOf(Scenario.WORD_FOUND, Scenario.ANSWER_SAVED)).mockModule(),
                GameKoinModule().module(),
                module()
                {
                    single<GameAnswerGateway> { answerRepository }
                    single<GameGuessGateway> { guessRepository }
                    single<AnnouncementRepresentable> { announcement }
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
        runBlocking { GameUseCase().setUpGameEvents() }

        // then
        keyboard.rows().first().forEach { assertEquals(GameKeyboard.Key.BackgroundColor.DEFAULT, it.backgroundColor()) }
    }

    @Test
    fun `when use case is invoked - GameBoard reset method is invoked`()
    {
        // given
        val gameBoard = runBlocking { GameUseCase().getGameBoard() }
        for (tile in gameBoard.rows().first())
        {
            tile.setLetter(newLetter = 'T')
            tile.setState(newState = GameBoard.Tile.State.INCORRECT)
        }
        gameBoard.setNewActiveRow()

        // when
        runBlocking { GameUseCase().setUpGameEvents() }

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
        val announcement: AnnouncementRepresentable by inject()
        announcement.setMessage(newMessage = "My Urgent Message!")

        // when
        runBlocking { GameUseCase().setUpGameEvents() }

        // then
        assertNull(announcement.message())
    }

    @Test
    fun `when use case is invoked and letter Key is clicked - GameBoard Tile is updated to expected letter`()
    {
        // given
        val expectedLetter = 'B'

        // when
        runBlocking { GameUseCase().setUpGameEvents() }
        for (row in keyboard.rows()) { for (key in row) { if (key.letter() == expectedLetter) runBlocking { key.click() } } }

        // then
        assertEquals(expectedLetter, gameBoard.rows().first().first().letter())
    }

    @Test
    fun `when use case is invoked and letter Key is clicked - handler onRevealNextTile method is invoked`()
    {
        // when
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler) }
        for (row in keyboard.rows()) { for (key in row) { if (key.letter() == 'Z') runBlocking { key.click() } } }

        // then
        assertTrue(sceneHandler.onRevealNextTileDidInvoke)
    }

    @Test
    fun `when use case is invoked and letter Key is clicked and GameBoard activeRow is unchanged - handler onRevealNextTile method is not invoked`()
    {
        // given
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler) }
        for (row in gameBoard.rows()) { for (tile in row) { runBlocking { getKey(letters = "C")?.click() } } }
        sceneHandler.onRevealNextTileDidInvoke = false

        // when
        runBlocking { getKey(letters = "C")?.click() }

        // then
        assertFalse(sceneHandler.onRevealNextTileDidInvoke)
    }

    @Test
    fun `when use case is invoked and GameBoard activeRow contains letters and backspace Key is clicked - GameBoard activeRow is updated to remove latest letter`()
    {
        // given
        runBlocking()
        {
            GameUseCase().setUpGameEvents(sceneHandler = sceneHandler)
            getKey(letters = "C")?.click()
        }
        sceneHandler.onRevealNextTileDidInvoke = false

        // when
        runBlocking { getKey(letters = "BACKSPACE")?.click() }

        // then
        assertNull(gameBoard.activeRow()?.firstOrNull { tile -> tile.letter() != null })
        assertTrue(sceneHandler.onRevealNextTileDidInvoke)
    }

    @Test
    fun `when use case is invoked and GameBoard activeRow contains no letters and backspace Key is clicked - activeRow remains empty`()
    {
        // given
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler) }

        // when
        runBlocking { getKey(letters = "BACKSPACE")?.click() }

        // then
        assertNull(gameBoard.activeRow()?.firstOrNull { tile -> tile.letter() != null })
        assertFalse(sceneHandler.onRevealNextTileDidInvoke)
    }

    @Test
    fun `when use case is invoked and enter Key is clicked - guessWord use case is invoked`()
    {
        // given
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler) }
        for (row in gameBoard.rows()) { for (tile in row) { runBlocking { getKey(letters = "C")?.click() } } }

        // when
        try { runBlocking { getKey(letters = "ENTER")?.click() } } catch(_ : Throwable) { }

        // then
        assertNotNull(guessRepository.guessToReturn)
        assertNotNull(answerRepository.gameAnswer)
    }

    @Test
    fun `when use case is invoked and enter Key is clicked and GameGuess is invalid - no exception is thrown`()
    {
        // given
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler) }
        runBlocking { getKey(letters = "C")?.click() }
        guessRepository.guessIsInvalid = true

        // when
        runBlocking { getKey(letters = "ENTER")?.click() }

        // then
        assertTrue(true)
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess contains an incorrect letter - GameKeyboard Key background colors are updated accordingly`()
    {
        // given
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler) }
        val keysInUse = listOf(
            getKey(letters = "P"), getKey(letters = "L"), getKey(letters = "A"), getKey(letters = "Y"), getKey(letters = "S")
        )

        // when
        for (key in keysInUse) runBlocking { key?.click() }
        runBlocking { getKey(letters = "ENTER")?.click() }

        // then
        for (key in keysInUse)
        {
            if (key?.letters() == "S") assertNotEquals(GameKeyboard.Key.BackgroundColor.NOT_FOUND, key.backgroundColor())
            else assertEquals(GameKeyboard.Key.BackgroundColor.NOT_FOUND, key?.backgroundColor())
        }

        for (row in keyboard.rows())
        {
            for (key in row)
            {
                if (!keysInUse.contains(key) && key.letter() != null)
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
        answerRepository.guessContainsCloseLetter = true
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler) }
        val keysInUse = listOf(
            getKey(letters = "P"), getKey(letters = "L"), getKey(letters = "A"), getKey(letters = "Y"), getKey(letters = "S")
        )

        // when
        for (key in keysInUse) runBlocking { key?.click() }
        runBlocking { getKey(letters = "ENTER")?.click() }

        // then
        for (key in keysInUse)
        {
            if ("SPEAR".contains(key?.letters()?.first()!!)) assertEquals(GameKeyboard.Key.BackgroundColor.NEARBY, key.backgroundColor())
            else assertNotEquals(GameKeyboard.Key.BackgroundColor.NEARBY, key.backgroundColor())
        }
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess contains a correct letter - GameKeyboard Key background colors are updated accordingly`()
    {
        // given
        guessRepository.guessContainsMatchingLetters = true
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler) }
        val keysInUse = listOf(
            getKey(letters = "T"), getKey(letters = "R"), getKey(letters = "E"), getKey(letters = "A"), getKey(letters = "T")
        )

        // when
        for (key in keysInUse) runBlocking { key?.click() }
        runBlocking { getKey(letters = "ENTER")?.click() }

        // then
        for (key in keysInUse)
        {
            if (key!!.letters() == "T")
            {
                assertEquals(GameKeyboard.Key.BackgroundColor.CORRECT, key.backgroundColor())
            }
            else assertNotEquals(GameKeyboard.Key.BackgroundColor.CORRECT, key.backgroundColor())
        }
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is not found in words list - expected announcement is shown and subsequently hidden`()
    {
        // given
        guessRepository.guessNotFound = true
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
        val keysInUse = listOf(
            getKey(letters = "T"), getKey(letters = "R"), getKey(letters = "E"), getKey(letters = "A"), getKey(letters = "T")
        )
        val expectedAnnouncementMessage = "Not found in words list."

        // when
        for (key in keysInUse) runBlocking { key?.click() }
        runBlocking { getKey(letters = "ENTER")?.click() }

        // then
        assertEquals(expectedAnnouncementMessage, announcement.previouslySetMessages.first())
        assertTrue(sceneHandler.onAnnouncementShouldShowDidInvoke)
        assertTrue(sceneHandler.onAnnouncementShouldHideDidInvoke)
        assertNull(announcement.message())
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is incorrect - GameBoard activeRow tiles are updated with expected states`()
    {
        // given
        answerRepository.guessContainsCloseLetter = true  // Answer: SPEAR
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler) }
        val initialActiveGameBoardRow = gameBoard.activeRow()
        val keysInUse = listOf(
            getKey(letters = "T"), getKey(letters = "R"), getKey(letters = "E"), getKey(letters = "A"), getKey(letters = "T")
        )
        val expectedEndOfRoundTilesStates = listOf(
            GameBoard.Tile.State.INCORRECT, GameBoard.Tile.State.CLOSE,
            GameBoard.Tile.State.CORRECT, GameBoard.Tile.State.CORRECT, GameBoard.Tile.State.INCORRECT
        )

        // when
        for (key in keysInUse) runBlocking { key?.click() }
        runBlocking { getKey(letters = "ENTER")?.click() }

        // then
        initialActiveGameBoardRow?.forEachIndexed()
        { index, tile ->
            assertEquals(expectedEndOfRoundTilesStates[index], tile.state())
        }
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is incorrect - GameBoard setNewActiveRow method is invoked`()
    {
        // given
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler) }
        val initialActiveGameBoardRow = gameBoard.activeRow()
        val keysInUse = listOf(
            getKey(letters = "T"), getKey(letters = "R"), getKey(letters = "E"), getKey(letters = "A"), getKey(letters = "T")
        )

        // when
        for (key in keysInUse) runBlocking { key?.click() }
        runBlocking { getKey(letters = "ENTER")?.click() }

        // then
        assertNotEquals(initialActiveGameBoardRow, gameBoard.activeRow())
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is incorrect - handler onRoundCompleted method is invoked`()
    {
        // given
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler) }
        val keysInUse = listOf(
            getKey(letters = "T"), getKey(letters = "R"), getKey(letters = "E"), getKey(letters = "A"), getKey(letters = "T")
        )

        // when
        for (key in keysInUse) runBlocking { key?.click() }
        runBlocking { getKey(letters = "ENTER")?.click() }

        // then
        assertTrue(sceneHandler.onRoundCompletedDidInvoke)
    }

    @Test
    fun `when GameBoard setNewActiveRow throws an exception - expected announcement is set`()
    {
        // given
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler) }
        val keysInUse = listOf(
            getKey(letters = "T"), getKey(letters = "R"), getKey(letters = "E"), getKey(letters = "A"), getKey(letters = "T")
        )
        val expectedAnnouncementMessage = "Game Over"

        // when
        for (round in gameBoard.rows())
        {
            for (key in keysInUse) runBlocking { key?.click() }
            runBlocking { getKey(letters = "ENTER")?.click() }
        }

        // then
        assertEquals(expectedAnnouncementMessage, announcement.message())
    }

    @Test
    fun `when GameBoard setNewActiveRow throws an exception - handler onGameOver method is invoked`()
    {
        // given
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler) }
        val keysInUse = listOf(
            getKey(letters = "T"), getKey(letters = "R"), getKey(letters = "E"), getKey(letters = "A"), getKey(letters = "T")
        )

        // when
        for (round in gameBoard.rows())
        {
            for (key in keysInUse) runBlocking { key?.click() }
            runBlocking { getKey(letters = "ENTER")?.click() }
        }

        // then
        assertTrue(sceneHandler.onGameOverDidInvoke)
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is correct - expected announcement is set`()
    {
        answerRepository.guessMatchesAnswer = true
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler) }
        val keysInUse = listOf(
            getKey(letters = "P"), getKey(letters = "L"), getKey(letters = "A"), getKey(letters = "Y"), getKey(letters = "S")
        )

        // when
        for (key in keysInUse) runBlocking { key?.click() }
        runBlocking { getKey(letters = "ENTER")?.click() }

        // then
        assertEquals("Correct! Thanks for playing!", announcement.message())
        assertTrue(sceneHandler.onGameOverDidInvoke)
    }

    data class MockAnnouncement(private var message: String? = null): AnnouncementRepresentable
    {
        var previouslySetMessages: MutableList<String> = mutableListOf()

        override fun message(): String? = this.message

        override fun setMessage(newMessage: String?)
        {
            newMessage?.let { previouslySetMessages.add(newMessage) }
            this.message = newMessage
        }
    }

    class MockSceneHandler: GameSceneHandleable
    {
        var onGameOverDidInvoke = false
        var onGameStartedDidInvoke = false
        var onGuessingWordDidInvoke = false
        var onRevealNextTileDidInvoke = false
        var onRoundCompletedDidInvoke = false
        var onStartingGameDidInvoke = false
        var onAnnouncementShouldShowDidInvoke = false
        var onAnnouncementShouldHideDidInvoke = false

        override fun onAnnouncementShouldShow() { onAnnouncementShouldShowDidInvoke = true }
        override fun onAnnouncementShouldHide() { onAnnouncementShouldHideDidInvoke = true }
        override fun onGameOver() { onGameOverDidInvoke = true }
        override fun onGameStarted() { onGameStartedDidInvoke = true }
        override fun onGuessingWord() { onGuessingWordDidInvoke = true }
        override fun onRevealNextTile() { onRevealNextTileDidInvoke = true }
        override fun onRoundCompleted() { onRoundCompletedDidInvoke = true }
        override fun onStartingGame() { onStartingGameDidInvoke = true }
    }

    private fun getKey(letters: String): GameKeyboard.Key?
    {
        val row = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letters() == letters } != null }
        return row?.first { it.letters() == letters }
    }

    class MockGameGuessRepository: GameGuessGateway
    {
        var guessIsInvalid = false
        var guessNotFound = false
        var guessContainsMatchingLetters = false
        var guessToReturn: GameGuess? = null

        override suspend fun create(): GameGuess
        {
            if (guessNotFound) throw GameGuessNotFoundRepositoryException("Not found in words list.")

            guessToReturn = if (guessIsInvalid) GameGuess(word = "T")
            else if (guessContainsMatchingLetters) GameGuess(word = "TREAT")
            else GameGuess(word = "PLAYS")

            return guessToReturn!!
        }

        override fun getAll(): List<GameGuess> = listOf()
        override suspend fun clear() { }
    }

    class MockGameAnswerRepository: GameAnswerGateway
    {
        var createdGameAnswer: GameAnswer? = null
        var gameAnswer: GameAnswer? = null
        var getShouldFail = false
        var guessMatchesAnswer = false
        var guessContainsCloseLetter = false

        override suspend fun create(): GameAnswer
        {
            createdGameAnswer = if (guessMatchesAnswer) GameAnswer(word = "PLAYS")
            else if (guessContainsCloseLetter) GameAnswer(word = "SPEAR")
            else GameAnswer(word = "TESTS")

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