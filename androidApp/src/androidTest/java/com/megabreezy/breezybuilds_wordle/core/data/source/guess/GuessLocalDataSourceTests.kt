package com.megabreezy.breezybuilds_wordle.core.data.source.guess

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithContentDescription
import com.megabreezy.breezybuilds_wordle.core.domain.model.Guess
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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
            copyToRealm(CachedGuess()).apply()
            {
                word = Word(word = "GUESS1")
            }
            copyToRealm(CachedGuess()).apply()
            {
                word = Word(word = "GUESS2")
            }
            copyToRealm(CachedGuess()).apply()
            {
                word = Word(word = "GUESS3")
            }
        }

        // when
        composeTestRule.setContent()
        {
            dataSource = remember { GuessLocalDataSource(realm = realm) }

            SceneMock.display { MockGuessLocalDataSourceTestView(localDataSource = dataSource) }
        }

        // then
        composeTestRule.onNodeWithContentDescription("GET_ALL_GUESSES").onChildAt(index = 0).assertTextEquals("GUESS1")
        composeTestRule.onNodeWithContentDescription("GET_ALL_GUESSES").onChildAt(index = 1).assertTextEquals("GUESS2")
        composeTestRule.onNodeWithContentDescription("GET_ALL_GUESSES").onChildAt(index = 2).assertTextEquals("GUESS3")
    }

    class MockGuessLocalDataSourceTestView(
        private val localDataSource: GuessLocalDataSource
    )
    {
        var getAllGuessList: List<Guess>? = null

        @Composable
        fun View()
        {
            LaunchedEffect(Unit)
            {
                getAllGuessList = localDataSource.getAll()
            }

            Column(modifier = Modifier.semantics { contentDescription = "GET_ALL_GUESSES" })
            {
                getAllGuessList?.forEach()
                {
                    Text(text = it.word().toString())
                }
            }
        }
    }
}