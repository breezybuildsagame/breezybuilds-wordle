package com.megabreezy.breezybuilds_wordle.core.data.source.completed_game

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.CompletedGame
import com.megabreezy.breezybuilds_wordle.core.domain.model.Guess
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CompletedGameLocalDataSourceTests
{
    private lateinit var realm: Realm

    private val mockGamesList = listOf(
        CompletedGame(
            answer = Answer(word = Word("TOAST"), playerGuessedCorrectly = true),
            date = 1680315545,
            playerGuesses = listOf(
                Guess(word = Word(word = "TASTE")),
                Guess(word = Word(word = "TRITE")),
                Guess(word = Word(word = "TOAST"))
            )
        ),
        CompletedGame(
            answer = Answer(word = Word("SLAPS"), playerGuessedCorrectly = false),
            date = 1680325545,
            playerGuesses = listOf(
                Guess(word = Word(word = "TEARS")),
                Guess(word = Word(word = "TRITE")),
                Guess(word = Word(word = "TOAST"))
            )
        )
    )

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp()
    {
        val config = RealmConfiguration.Builder(setOf(CachedCompletedGame::class))
            .inMemory()
            .build()
        realm = Realm.open(configuration = config)
    }

    @After
    fun tearDown()
    {
        realm.writeBlocking()
        {
            val writeTransactionCachedAnswers = query<CachedCompletedGame>().find()
            delete(writeTransactionCachedAnswers)
        }
        realm.close()
    }

    @Test
    fun when_getAll_method_invoked_and_results_found__expected_results_list_is_returned()
    {
        // given
        lateinit var localDataSource: CompletedGameLocalDataSource
        val expectedCompletedGames = mockGamesList

        realm.writeBlocking()
        {
            expectedCompletedGames.forEach()
            {
                val guessesToSave = realmListOf<String>()
                it.playerGuesses().forEach { guess -> guessesToSave.add("${guess.word()}") }

                copyToRealm(
                    CachedCompletedGame().apply {
                        answer = it.answer()
                        date = it.date()
                        playerGuesses = guessesToSave
                        playerGuessedCorrectly = it.answer().playerGuessedCorrectly() ?: false
                        word = "${it.answer().word()}"
                    }
                )
            }
        }

        // when
        composeTestRule.setContent()
        {
            localDataSource = CompletedGameLocalDataSource(realm = realm)

            SceneMock.display { MockView.Component(localDataSource = localDataSource) }
        }
        val gamesList = composeTestRule.onNodeWithContentDescription("COLUMN").onChildren()

        // then
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("TAG")
        gamesList.assertCountEquals(expectedCompletedGames.count())
        expectedCompletedGames.forEachIndexed()
        { index, game ->
            gamesList[index].assertTextEquals("${game.answer().word()}")
        }
    }

    @Test
    fun when_getAll_method_invoked_and_data_source_returns_no_results__no_exception_is_thrown()
    {
        // given
        lateinit var localDataSource: CompletedGameLocalDataSource

        // when
        composeTestRule.setContent()
        {
            localDataSource = CompletedGameLocalDataSource(realm = realm)

            SceneMock.display { MockView.Component(localDataSource = localDataSource) }
        }

        // then
        composeTestRule.onNodeWithContentDescription("COLUMN").onChild().assertDoesNotExist()
    }

    @Test
    fun when_put_method_invoked_with_newCompletedGame__expected_CompletedGame_is_returned()
    {
        // given
        lateinit var localDataSource: CompletedGameLocalDataSource
        val expectedCompletedGame = mockGamesList.last()

        // when
        composeTestRule.setContent()
        {
            localDataSource = CompletedGameLocalDataSource(realm = realm)
            runBlocking { localDataSource.put(newCompletedGame = expectedCompletedGame) }

            SceneMock.display { MockView.Component(localDataSource = localDataSource) }
        }
        val gamesList = composeTestRule.onNodeWithContentDescription("COLUMN").onChildren()

        // then
        composeTestRule.onRoot(useUnmergedTree = true).printToLog("TAG")
        gamesList.assertCountEquals(1)
        gamesList.onFirst().assertContentDescriptionEquals("${expectedCompletedGame.date()}")
        gamesList.onFirst().assertTextEquals("${expectedCompletedGame.answer().word()}")
    }

    object MockView
    {
        @Composable
        fun Component(
            localDataSource: CompletedGameLocalDataSource,
            newCompletedGame: CompletedGame? = null
        )
        {
            var completedGames by remember { mutableStateOf<List<CompletedGame>>(listOf()) }
            var updatedCompletedGames by remember { mutableStateOf<List<CompletedGame>>(listOf()) }

            LaunchedEffect(Unit)
            {
                updatedCompletedGames = newCompletedGame?.let { listOf(localDataSource.put(newCompletedGame = it)) } ?: listOf()

                completedGames = localDataSource.getAll()
            }

            Column(modifier = Modifier.fillMaxSize().semantics { contentDescription = "COLUMN" })
            {
                completedGames.forEach()
                {
                    Text(
                        text = "${it.answer().word()}",
                        modifier = Modifier.semantics { contentDescription = "${it.date()}" }
                    )
                }
            }
        }
    }
}