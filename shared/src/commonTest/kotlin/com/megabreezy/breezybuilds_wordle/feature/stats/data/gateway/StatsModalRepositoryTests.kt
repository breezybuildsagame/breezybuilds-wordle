package com.megabreezy.breezybuilds_wordle.feature.stats.data.gateway

import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.mock.CompletedGameLocalDataSourceCommonMock
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.CompletedGame
import com.megabreezy.breezybuilds_wordle.core.domain.model.Guess
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.GuessDistribution
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.Stat
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.StatsModal
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class StatsModalRepositoryTests
{
    lateinit var localDataSource: CompletedGameLocalDataSourceCommonMock

    @BeforeTest
    fun setUp()
    {
        localDataSource = CompletedGameLocalDataSourceCommonMock()

        startKoin()
        {
            modules(
                CoreKoinModule().mockModule(),
                module { single<CompletedGameLocalDataManageable> { localDataSource } }
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `When the get method is invoked - the CompletedGameLocalDataManageable getAll method is invoked`()
    {
        // given
        val sut = StatsModalRepository()

        // when
        sut.get()

        // then
        assertNotNull(localDataSource.getCompletedGamesToReturn)
    }

    @Test
    fun `When the getWinGroups method is invoked passing in a list of completedGames - expected groups are returned`()
    {
        // given
        val sut = StatsModalRepository()
        val game1 = CompletedGame(answer = Answer(word = Word("ROUND1"), playerGuessedCorrectly = true))
        val game2 = CompletedGame(answer = Answer(word = Word("ROUND2"), playerGuessedCorrectly = false))
        val game3 = CompletedGame(answer = Answer(word = Word("ROUND3"), playerGuessedCorrectly = true))
        val game4 = CompletedGame(answer = Answer(word = Word("ROUND4"), playerGuessedCorrectly = true))

        val expectedGroupedGames = listOf(listOf(game1), listOf(game3, game4))

        // when
        val actualGroupedGames = sut.getWinGroups(gamesList = listOf(game1, game2, game3, game4))

        // then
        assertEquals(expectedGroupedGames, actualGroupedGames)
    }

    @Test
    fun `When getWinsByRound method is invoked passing in a list of completedGames - expectedGroups are returned`()
    {
        // given
        val sut = StatsModalRepository()
        val game1 = CompletedGame(answer = Answer(word = Word("ROUND1"), playerGuessedCorrectly = true),
                                  playerGuesses = listOf(Guess(Word("ROUND0")), Guess(Word("ROUND1")) ))
        val game2 = CompletedGame(answer = Answer(word = Word("ROUND2"), playerGuessedCorrectly = false),
                                  playerGuesses = listOf(Guess(Word("ROUND0")), Guess(Word("ROUND1")), Guess(Word("ROUND4")) ))
        val game3 = CompletedGame(answer = Answer(word = Word("ROUND3"), playerGuessedCorrectly = true),
                                  playerGuesses = listOf(Guess(Word("ROUND0")), Guess(Word("ROUND1")), Guess(Word("ROUND3")) ))
        val game4 = CompletedGame(answer = Answer(word = Word("ROUND4"), playerGuessedCorrectly = true),
                                  playerGuesses = listOf(Guess(Word("ROUND0")), Guess(Word("ROUND1")), Guess(Word("ROUND4")) ))
        val expectedWinsByRound = listOf(0, 1, 2)

        // when
        val actualWinsByRound = sut.getWinsByRound(gamesList = listOf(game1, game2, game3, game4))

        // then
        assertEquals(expectedWinsByRound, actualWinsByRound)
    }

    @Test
    fun `When the get method is invoked and a list of CompletedGames is returned - the expected StatusModal entity is returned`()
    {
        // given
        localDataSource.getCompletedGamesToReturn = mutableListOf(
            CompletedGame(
                answer = Answer(word= Word("ROUND1"), playerGuessedCorrectly = true),
                playerGuesses = listOf(
                    Guess(word = Word("ROUND2")),
                    Guess(word = Word("ROUND1"))
                )
            ),
            CompletedGame(
                answer = Answer(word= Word("ROUND2"), playerGuessedCorrectly = true),
                playerGuesses = listOf(
                    Guess(word = Word("ROUND3")),
                    Guess(word = Word("ROUND2"))
                )
            ),
            CompletedGame(
                answer = Answer(word= Word("ROUND3"), playerGuessedCorrectly = false),
                playerGuesses = listOf(
                    Guess(word = Word("ROUND2")),
                    Guess(word = Word("ROUND1")),
                    Guess(word = Word("ROUND4"))
                )
            ),
            CompletedGame(
                answer = Answer(word= Word("ROUND4"), playerGuessedCorrectly = true),
                playerGuesses = listOf(
                    Guess(word = Word("ROUND2")),
                    Guess(word = Word("ROUND1")),
                    Guess(word = Word("ROUND4"))
                )
            )
        )

        val expectedStatsModal = StatsModal(
            stats = listOf(
                Stat(headline = "4", description = "Played"),
                Stat(headline = "75", description = "Win %"),
                Stat(headline = "1", description = "Current Streak"),
                Stat(headline = "2", description = "Max Streak")
            ),
            guessDistribution = GuessDistribution(
                title = "Guess Distribution",
                rows = listOf(
                    GuessDistribution.Row(round = 1, correctGuessesCount = 0),
                    GuessDistribution.Row(round = 2, correctGuessesCount = 2),
                    GuessDistribution.Row(round = 3, correctGuessesCount = 1)
                )
            )
        )
        val sut = StatsModalRepository()

        // when
        val actualStatsModal = sut.get()

        // then
        assertEquals(expectedStatsModal, actualStatsModal)
    }
}