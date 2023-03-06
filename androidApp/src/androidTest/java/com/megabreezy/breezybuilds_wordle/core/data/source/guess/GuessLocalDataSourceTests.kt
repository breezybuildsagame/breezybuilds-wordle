package com.megabreezy.breezybuilds_wordle.core.data.source.guess

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.megabreezy.breezybuilds_wordle.core.domain.model.Guess
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import org.junit.*

class GuessLocalDataSourceTests
{
    private lateinit var realm: Realm

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp()
    {
        val config = RealmConfiguration.Builder(setOf(CachedGuess::class))
            .inMemory()
            .build()
        realm = Realm.open(configuration = config)
    }

    @After
    fun tearDown()
    {
        realm.writeBlocking()
        {
            val writeTransactionCachedAnswers = query<CachedGuess>().find()
            delete(writeTransactionCachedAnswers)
        }
        realm.close()
    }

    @Test
    fun when_getAll_method_invoked_and_guesses_found__expected_Guess_list_is_returned()
    {
        // given
        lateinit var dataSource: GuessLocalDataSource
        realm.writeBlocking()
        {
            copyToRealm(CachedGuess()).apply { word = "GUESS1" }
            copyToRealm(CachedGuess()).apply { word = "GUESS2" }
            copyToRealm(CachedGuess()).apply { word = "GUESS3" }
        }

        // when
        composeTestRule.setContent()
        {
            dataSource = remember { GuessLocalDataSource(realm = realm) }

            SceneMock.display { MockGuessLocalDataSourceTestView(localDataSource = dataSource).View() }
        }
        val allGuessesContainer = composeTestRule.onNodeWithContentDescription("GET_ALL_GUESSES", useUnmergedTree = true)

        // then=
        allGuessesContainer.onChildAt(index = 0).assertTextEquals("GUESS1")
        allGuessesContainer.onChildAt(index = 1).assertTextEquals("GUESS2")
        allGuessesContainer.onChildAt(index = 2).assertTextEquals("GUESS3")
    }

    @Test
    fun when_create_method_invoked_and_create_is_successful__expected_Guess_is_returned()
    {
        // given
        lateinit var dataSource: GuessLocalDataSource
        val expectedCreatedGuess = Guess(word = Word(word = "TRYING"))

        // when
        composeTestRule.setContent()
        {
            dataSource = remember { GuessLocalDataSource(realm = realm) }

            SceneMock.display()
            {
                MockGuessLocalDataSourceTestView(guessToCreate = expectedCreatedGuess, localDataSource = dataSource).View()
            }
        }

        // then
        composeTestRule.onNodeWithContentDescription("CREATED_GUESS").assertTextEquals("${expectedCreatedGuess.word()}")
    }

    @Test
    fun when_create_method_invoked_and_create_is_unsuccessful__expected_exception_is_thrown()
    {
        // given
        lateinit var dataSource: GuessLocalDataSource
        realm.writeBlocking()
        {
            copyToRealm(CachedGuess()).apply { word = "ALREADY" }
        }

        // when
        val actualException = Assert.assertThrows(GuessSaveFailedLocalDataException::class.java)
        {
            composeTestRule.setContent()
            {
                dataSource = remember { GuessLocalDataSource(realm = realm) }

                SceneMock.display()
                {
                    MockGuessLocalDataSourceTestView(guessToCreate = Guess(word = Word(word = "ALREADY")), localDataSource = dataSource).View()
                }
            }
        }

        // then
        Assert.assertEquals("Guess ALREADY has been previously guessed.", actualException.message)
    }

    @Test
    fun when_clear_method_invoked_and_cached_guesses_found__guesses_are_cleared()
    {
        // given
        lateinit var dataSource: GuessLocalDataSource
        realm.writeBlocking()
        {
            copyToRealm(CachedGuess()).apply { word = "GUESS1" }
            copyToRealm(CachedGuess()).apply { word = "GUESS2" }
            copyToRealm(CachedGuess()).apply { word = "GUESS3" }
        }

        // when
        composeTestRule.setContent()
        {
            dataSource = remember { GuessLocalDataSource(realm = realm) }

            SceneMock.display { MockGuessLocalDataSourceTestView(localDataSource = dataSource, shouldClearGuesses = true).View() }
        }
        val allGuessesContainer = composeTestRule.onNodeWithContentDescription("GET_ALL_GUESSES", useUnmergedTree = true)

        // then
        allGuessesContainer.onChildAt(index = 0).assertDoesNotExist()
    }

    class MockGuessLocalDataSourceTestView(
        private val guessToCreate: Guess? = null,
        private val localDataSource: GuessLocalDataSource,
        private val shouldClearGuesses: Boolean = false
    )
    {
        private var getAllGuessList by mutableStateOf<List<Guess>?>(null)
        private var createdGuess by mutableStateOf<Guess?>(null)

        @Composable
        fun View()
        {
            LaunchedEffect(Unit)
            {
                getAllGuessList = localDataSource.getAll()
                guessToCreate?.let { createdGuess = localDataSource.create(newGuess = "${it.word()}") }
                if (shouldClearGuesses)
                {
                    localDataSource.clear()
                    getAllGuessList = localDataSource.getAll()
                }
            }

            Column(modifier = Modifier.semantics { contentDescription = "GET_ALL_GUESSES" })
            {
                getAllGuessList?.forEach()
                {
                    Text(text = it.word().toString())
                }
            }

            Text(
                text = createdGuess?.let { "${it.word()}" } ?: "",
                modifier = Modifier.semantics { contentDescription = "CREATED_GUESS" }
            )
        }
    }
}