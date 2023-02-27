package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway

import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerUpdateFailedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotCreatedRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameAnswerRepository: GameAnswerGateway, KoinComponent
{
    private val answerLocalDataSource: AnswerLocalDataManageable by inject()
    private val wordLocalDataSource: WordLocalDataManageable by inject()

    override suspend fun create(): GameAnswer
    {
        val excludedWords = mutableListOf<Word>()
        answerLocalDataSource.getPrevious().forEach { excludedWords.add(Word(word = it.word().toString())) }

        val mostRecentAnswer: Answer? = try { answerLocalDataSource.getCurrent() } catch(e: Throwable) { null }

        try
        {
            mostRecentAnswer?.let { excludedWords.add(Word(word = it.word().toString())) }

            val word = wordLocalDataSource.get(excludingWords = excludedWords)

            answerLocalDataSource.insert(newAnswer = Answer(word = word, isCurrent = true))

            mostRecentAnswer?.let()
            { previousAnswer ->
                val updatedPreviousAnswer = Answer(word = previousAnswer.word(), isCurrent = previousAnswer.isCurrent())
                updatedPreviousAnswer.setIsCurrent(newIsCurrent = false)
                answerLocalDataSource.update(existingAnswer = previousAnswer, updatedAnswer = updatedPreviousAnswer)
            }

            return GameAnswer(word = word.toString())
        }
        catch(e: Throwable)
        {
            when(e)
            {
                is AnswerUpdateFailedLocalDataException, is WordNotFoundLocalDataException ->
                {
                    throw GameAnswerNotCreatedRepositoryException(message = e.message)
                }
                else -> throw Exception("Uncaught error: ${e.message}")
            }
        }
    }

    override fun get(): GameAnswer
    {
        try
        {
            val answer = answerLocalDataSource.getCurrent()

            return GameAnswer(word = answer.word().toString())
        }
        catch(e: AnswerNotFoundLocalDataException)
        {
            throw GameAnswerNotFoundRepositoryException(message = e.message)
        }
    }
}