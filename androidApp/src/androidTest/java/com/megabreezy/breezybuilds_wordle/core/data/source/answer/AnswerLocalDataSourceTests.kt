package com.megabreezy.breezybuilds_wordle.core.data.source.answer

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AnswerLocalDataSourceTests
{
    private lateinit var realm: Realm

    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp()
    {
        val config = RealmConfiguration.Builder(setOf(CachedAnswer::class))
            .inMemory()
            .build()
        realm = Realm.open(configuration = config)
    }

    @After
    fun tearDown()
    {
        realm.writeBlocking()
        {
            val writeTransactionCachedAnswers = query<CachedAnswer>().find()
            delete(writeTransactionCachedAnswers)
        }
        realm.close()
    }

    @Test
    fun when_getCurrent_method_invoked_and_cached_result_is_found__expected_answer_is_returned()
    {
        // given
        lateinit var dataSource: AnswerLocalDataSource
        val expectedAnswer = "STRAY"

        realm.writeBlocking()
        {
            copyToRealm(
                CachedAnswer().apply()
                {
                    word = expectedAnswer
                    isCurrent = true
                }
            )
            copyToRealm(
                CachedAnswer().apply()
                {
                    word = "NOPE"
                    isCurrent = false
                }
            )
        }

        // when
        composeTestRule.setContent()
        {
            dataSource = remember { AnswerLocalDataSource(realm = realm) }

            SceneMock.display { MockView().View(dataSource = dataSource) }
        }

        // then
        composeTestRule.onNodeWithContentDescription("CURRENT_ANSWER").assertTextEquals(expectedAnswer)
    }

    @Test
    fun when_getCurrent_method_invoked_and_cached_result_not_found__expected_exception_is_thrown()
    {
        // given
        lateinit var dataSource: AnswerLocalDataSource
        val expectedExceptionMessage = "No current answer found."

        // when
        composeTestRule.setContent()
        {
            dataSource = remember { AnswerLocalDataSource(realm = realm) }

            SceneMock.display { MockView().View(dataSource = dataSource) }
        }

        // then
        composeTestRule.onNodeWithContentDescription("CURRENT_ANSWER").assertTextEquals(expectedExceptionMessage)
    }

    @Test
    fun when_getPrevious_method_invoked_and_previous_cached_answers_found__expected_answer_list_is_returned()
    {
        // given
        lateinit var dataSource: AnswerLocalDataSource
        val expectedPreviousAnswerList = listOf("ANSWER1", "ANSWER2")
        realm.writeBlocking()
        {
            copyToRealm(
                CachedAnswer().apply()
                {
                    word = expectedPreviousAnswerList[0]
                    isCurrent = false
                }
            )
            copyToRealm(
                CachedAnswer().apply()
                {
                    word = expectedPreviousAnswerList[1]
                    isCurrent = false
                }
            )
            copyToRealm(
                CachedAnswer().apply()
                {
                    word = "ANSWER3"
                    isCurrent = true
                }
            )
        }

        // when
        composeTestRule.setContent()
        {
            dataSource = remember { AnswerLocalDataSource(realm = realm) }

            SceneMock.display { MockView().View(dataSource = dataSource) }
        }
        val previousAnswerListContainer = composeTestRule.onNodeWithContentDescription("PREVIOUS_ANSWERS", useUnmergedTree = true)

        // then
        previousAnswerListContainer.onChildAt(index = 0).assertTextEquals(expectedPreviousAnswerList[0])
        previousAnswerListContainer.onChildAt(index = 1).assertTextEquals(expectedPreviousAnswerList[1])
        previousAnswerListContainer.onChildAt(index = 2).assertDoesNotExist()
    }

    @Test
    fun when_getPrevious_method_invoked_and_cached_result_not_found__empty_list_is_returned()
    {
        // given
        lateinit var dataSource: AnswerLocalDataSource

        // when
        composeTestRule.setContent()
        {
            dataSource = remember { AnswerLocalDataSource(realm = realm) }

            SceneMock.display { MockView().View(dataSource = dataSource) }
        }

        // then
        composeTestRule.onNodeWithContentDescription("PREVIOUS_ANSWERS", useUnmergedTree = true).onChildAt(index = 0).assertDoesNotExist()
    }

    class MockView
    {
        @Composable
        fun View(
            dataSource: AnswerLocalDataSource
        )
        {
            var currentAnswer by remember { mutableStateOf("") }
            var previousAnswers by remember { mutableStateOf<List<Answer>>(listOf()) }

            LaunchedEffect(Unit)
            {
                currentAnswer = try { dataSource.getCurrent().word().toString() } catch(e: Throwable) { e.message ?: "" }
                previousAnswers = dataSource.getPrevious()
            }

            Text(
                text = currentAnswer,
                modifier = Modifier.semantics { contentDescription = "CURRENT_ANSWER" }
            )

            Column(modifier = Modifier.semantics { contentDescription = "PREVIOUS_ANSWERS" })
            {
                previousAnswers.forEach()
                {
                    Text(text = it.word().toString())
                }
            }
        }
    }
}