package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.mock.GameAnswerRepositoryCommonMock
import com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.mock.GameGuessRepositoryCommonMock
import com.megabreezy.breezybuilds_wordle.feature.game.data.gateway.mock.SavedGameRepositoryCommonMock
import com.megabreezy.breezybuilds_wordle.feature.game.domain.GameNavigationHandleable
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.*
import com.megabreezy.breezybuilds_wordle.feature.game.domain.mock.GameNavigationHandlerCommonMock
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.*
import com.megabreezy.breezybuilds_wordle.feature.game.presentation.mock.GameSceneHandlerCommonMock
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
class SetUpGameEventsTests: KoinComponent
{
    private lateinit var answerRepository: GameAnswerRepositoryCommonMock
    private lateinit var guessRepository: GameGuessRepositoryCommonMock
    private lateinit var savedGameRepository: SavedGameRepositoryCommonMock
    private lateinit var sceneHandler: GameSceneHandlerCommonMock
    private lateinit var announcement: MockAnnouncement
    private lateinit var gameNavigationHandler: GameNavigationHandlerCommonMock

    private val gameBoard: GameBoard by inject()
    private val keyboard: GameKeyboard by inject()

    @BeforeTest
    fun setUp()
    {
        answerRepository = GameAnswerRepositoryCommonMock()
        guessRepository = GameGuessRepositoryCommonMock()
        savedGameRepository = SavedGameRepositoryCommonMock()
        sceneHandler = GameSceneHandlerCommonMock()
        announcement = MockAnnouncement()
        gameNavigationHandler = GameNavigationHandlerCommonMock()

        startKoin()
        {
            modules(
                CoreKoinModule(scenarios = listOf(Scenario.WORD_FOUND, Scenario.ANSWER_SAVED)).mockModule(),
                GameKoinModule().module(),
                module()
                {
                    single<GameAnswerGateway> { answerRepository }
                    single<GameGuessGateway> { guessRepository }
                    single<GameNavigationHandleable> { gameNavigationHandler }
                    single<AnnouncementRepresentable> { announcement }
                    single<SavedGameGateway> { savedGameRepository }
                }
            )
        }
        answerRepository.getShouldFail = true
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
        runBlocking { GameUseCase().setUpGameEvents(announcementDelay = 0L) }

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
        runBlocking { GameUseCase().setUpGameEvents(announcementDelay = 0L) }

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
        runBlocking { GameUseCase().setUpGameEvents(announcementDelay = 0L) }

        // then
        assertNull(announcement.message())
    }

    @Test
    fun `when use case is invoked and letter Key is clicked - GameBoard Tile is updated to expected letter`()
    {
        // given
        val expectedLetter = 'B'

        // when
        runBlocking { GameUseCase().setUpGameEvents(announcementDelay = 0L) }
        for (row in keyboard.rows()) { for (key in row) { if (key.letter() == expectedLetter) runBlocking { key.click() } } }

        // then
        assertEquals(expectedLetter, gameBoard.rows().first().first().letter())
    }

    @Test
    fun `when use case is invoked and letter Key is clicked - handler onRevealNextTile method is invoked`()
    {
        // when
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
        for (row in keyboard.rows()) { for (key in row) { if (key.letter() == 'Z') runBlocking { key.click() } } }

        // then
        assertTrue(sceneHandler.onRevealNextTileDidInvoke)
    }

    @Test
    fun `when use case is invoked and letter Key is clicked and GameBoard activeRow is unchanged - handler onRevealNextTile method is not invoked`()
    {
        // given
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
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
            GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L)
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
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }

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
        answerRepository.getShouldFail = false
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
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
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
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
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
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
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
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
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
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
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
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
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
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
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
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
    fun `when GameBoard setNewActiveRow throws an exception - injected CompletedGameGateway's put method is invoked`()
    {
        // given
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
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
        assertNotNull(savedGameRepository.savedGameToReturn)
    }

    @Test
    fun `when GameBoard setNewActiveRow throws an exception - injected GameGuessGateway's clear method is invoked`()
    {
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
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
        assertTrue(guessRepository.clearDidInvoke)
    }

    @Test
    fun `when GameBoard setNewActiveRow throws an exception - expected announcement is set`()
    {
        // given
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
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
    fun `When onGameOver invoked after unsuccessful game - injected GameNavigationHandleable onGameOver method is invoked`()
    {
        // given
        val expectedDelay = 10L
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = expectedDelay) }
        val keysInUse = listOf(
            getKey(letters = "T"), getKey(letters = "R"), getKey(letters = "E"), getKey(letters = "A"), getKey(letters = "T")
        )
        var actualTime = Duration.ZERO

        // when
        for (round in gameBoard.rows())
        {
            for (key in keysInUse) runBlocking { key?.click() }

            actualTime = measureTime()
            {
                runBlocking { getKey(letters = "ENTER")?.click() }
            }
        }

        assertTrue(gameNavigationHandler.onGameOverDidInvoke)
        assertTrue(actualTime.inWholeMilliseconds > expectedDelay - 1)
    }

    @Test
    fun `when GameBoard setNewActiveRow throws an exception - Answer is updated to expected state`()
    {
        // given
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
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
        assertTrue(answerRepository.updateAnswerNotGuessedDidInvoke)
        assertFalse(answerRepository.updateAnswerGuessedDidInvoke)
        assertEquals(answerRepository.createdGameAnswer, answerRepository.updatedGameAnswerToReturn)
    }

    @Test
    fun `when GameBoard setNewActiveRow throws an exception - handler onGameOver method is invoked`()
    {
        // given
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
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
    fun `when use case invoked and enter Key is clicked and GameGuess is correct - Answer is updated to expected state`()
    {
        // given
        answerRepository.guessMatchesAnswer = true
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
        val keysInUse = listOf(
            getKey(letters = "P"), getKey(letters = "L"), getKey(letters = "A"), getKey(letters = "Y"), getKey(letters = "S")
        )

        // when
        for (key in keysInUse) runBlocking { key?.click() }
        runBlocking { getKey(letters = "ENTER")?.click() }

        // then
        assertTrue(answerRepository.updateAnswerGuessedDidInvoke)
        assertFalse(answerRepository.updateAnswerNotGuessedDidInvoke)
        assertEquals(answerRepository.createdGameAnswer, answerRepository.updatedGameAnswerToReturn)
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is correct - injected CompletedGameGateway's put method is invoked`()
    {
        // given
        answerRepository.guessMatchesAnswer = true
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
        val keysInUse = answerRepository.createdGameAnswer!!.word().indices.map()
        {
            getKey(letters = "${answerRepository.createdGameAnswer!!.word()[it]}" )
        }

        // when
        for (key in keysInUse) runBlocking { key?.click() }
        runBlocking { getKey(letters = "ENTER")?.click() }

        // then
        assertNotNull(savedGameRepository.savedGameToReturn)
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is correct - injected GameGuessGateway's clear method is invoked`()
    {
        // given
        answerRepository.guessMatchesAnswer = true
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
        val keysInUse = answerRepository.createdGameAnswer!!.word().indices.map()
        {
            getKey(letters = "${answerRepository.createdGameAnswer!!.word()[it]}" )
        }

        // when
        for (key in keysInUse) runBlocking { key?.click() }
        runBlocking { getKey(letters = "ENTER")?.click() }

        // then
        assertTrue(guessRepository.clearDidInvoke)
    }

    @Test
    fun `when use case invoked and enter Key is clicked and GameGuess is correct - expected announcement is set`()
    {
        // given
        answerRepository.guessMatchesAnswer = true
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
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

    @Test
    fun `When onGameOver invoked after successful game - injected GameNavigationHandleable onGameOver method is invoked`()
    {
        // given
        val expectedDelay = 10L
        answerRepository.guessMatchesAnswer = true
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = expectedDelay) }
        val keysInUse = listOf(
            getKey(letters = "P"), getKey(letters = "L"), getKey(letters = "A"), getKey(letters = "Y"), getKey(letters = "S")
        )

        // when
        for (key in keysInUse) runBlocking { key?.click() }
        val actualTime = measureTime()
        {
            runBlocking { getKey(letters = "ENTER")?.click() }
        }

        // then
        assertTrue(gameNavigationHandler.onGameOverDidInvoke)
        assertTrue(actualTime.inWholeMilliseconds > expectedDelay - 1)
    }

    @Test
    fun `When use case invoked and game in progress - GameBoard matches expected state`()
    {
        // given
        answerRepository.getShouldFail = false
        answerRepository.gameAnswer = GameAnswer(word = "STARS")
        guessRepository.getAllGuessesToReturn = listOf(
            GameGuess(word = "SLAYS")
        )
        val expectedGameBoardRows = listOf(
            listOf(
                GameBoard.Tile(letter = 'S', state = GameBoard.Tile.State.CORRECT),
                GameBoard.Tile(letter = 'L', state = GameBoard.Tile.State.INCORRECT),
                GameBoard.Tile(letter = 'A', state = GameBoard.Tile.State.CORRECT),
                GameBoard.Tile(letter = 'Y', state = GameBoard.Tile.State.INCORRECT),
                GameBoard.Tile(letter = 'S', state = GameBoard.Tile.State.CORRECT)
            ),
            listOf(GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile()),
            listOf(GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile()),
            listOf(GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile()),
            listOf(GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile()),
            listOf(GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile(), GameBoard.Tile())
        )

        // when
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }
        val actualGameBoard = runBlocking { GameUseCase().getGameBoard() }

        // then
        assertEquals(expectedGameBoardRows, actualGameBoard.rows())
        assertEquals(expectedGameBoardRows[1], actualGameBoard.activeRow())
    }

    @Test
    fun `When use case invoked and game in progress - GameKeyboard matches expected state`()
    {
        // given
        answerRepository.getShouldFail = false
        answerRepository.gameAnswer = GameAnswer(word = "STARS")
        guessRepository.getAllGuessesToReturn = listOf(
            GameGuess(word = "SLAYS")
        )

        // when
        runBlocking { GameUseCase().setUpGameEvents(sceneHandler = sceneHandler, announcementDelay = 0L) }

        // then
        val keys = GameUseCase().getGameKeyboard().rows().flatten().filter { it.backgroundColor() != GameKeyboard.Key.BackgroundColor.DEFAULT }

        // then
        assertEquals(4, keys.count()) // 4 = unique letters guessed
        for (key in keys)
        {
            when (key.letter())
            {
                'S' -> assertEquals(GameKeyboard.Key.BackgroundColor.CORRECT, key.backgroundColor())
                'L' -> assertEquals(GameKeyboard.Key.BackgroundColor.NOT_FOUND, key.backgroundColor())
                'A' -> assertEquals(GameKeyboard.Key.BackgroundColor.CORRECT, key.backgroundColor())
                'Y' -> assertEquals(GameKeyboard.Key.BackgroundColor.NOT_FOUND, key.backgroundColor())
            }
        }
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

    private fun getKey(letters: String): GameKeyboard.Key?
    {
        val row = keyboard.rows().firstOrNull { it.firstOrNull { key -> key.letters() == letters } != null }
        return row?.first { it.letters() == letters }
    }
}