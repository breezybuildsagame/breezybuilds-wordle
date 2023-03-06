package com.megabreezy.breezybuilds_wordle.core.data.source.word

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class WordLocalDataSourceTests
{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setUp()
    {
        startKoin { modules(CoreKoinModule().module()) }
    }

    @After
    fun tearDown() = stopKoin()

    @Test
    fun when_getAll_method_invoked_and_words_found__expected_Word_list_is_returned()
    {
        // given
        lateinit var dataSource: WordLocalDataSource

        // when
        composeTestRule.setContent()
        {
            dataSource = remember { WordLocalDataSource() }
            dataSource.fileReader.context = LocalContext.current

            SceneMock.display { MockWordView(localDataSource = dataSource).View() }
        }

        // then
        composeTestRule.onNodeWithContentDescription("WORDS_LIST").assertExists()
    }

    @Test
    fun when_get_method_invoked_and_excluded_list_included__expected_Word_is_returned()
    {
        // given
        lateinit var dataSource: WordLocalDataSource
        lateinit var expectedWord: Word

        // when
        composeTestRule.setContent()
        {
            dataSource = remember { WordLocalDataSource() }
            dataSource.fileReader.context = LocalContext.current
            val allWords = dataSource.getAll()
            expectedWord = allWords.last()

            SceneMock.display { MockWordView(localDataSource = dataSource, excludedWordsList = allWords.dropLast(1)).View() }
        }

        // then
        composeTestRule.onNodeWithContentDescription("FOUND_WORD").assertExists().assertTextEquals(expectedWord.word())
    }

    class MockWordView(private val localDataSource: WordLocalDataSource, private val excludedWordsList: List<Word> = listOf())
    {
        var foundWord by mutableStateOf<Word?>(null)
        var wordsList by mutableStateOf<List<Word>?>(null)

        @Composable
        fun View()
        {
            LaunchedEffect(Unit)
            {
                foundWord = if (excludedWordsList.isNotEmpty()) localDataSource.get(excludingWords = excludedWordsList) else null
                wordsList = localDataSource.getAll()
            }

            foundWord?.let()
            {
                Text(text = it.word(), modifier = Modifier.semantics { contentDescription = "FOUND_WORD" })
            }
            ?:
            wordsList?.let()
            {
                LazyColumn(
                    modifier = Modifier.semantics { contentDescription = "WORDS_LIST" }
                )
                {
                    items(
                        items = it,
                        itemContent = {
                            Text(text = it.word())
                        }
                    )
                }
            }
        }
    }
}