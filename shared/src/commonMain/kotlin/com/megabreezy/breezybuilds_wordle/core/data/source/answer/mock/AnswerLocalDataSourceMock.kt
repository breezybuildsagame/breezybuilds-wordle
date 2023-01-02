package com.megabreezy.breezybuilds_wordle.core.data.source.answer.mock

import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerSaveFailedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerUpdateFailedLocalDataException
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import org.koin.core.module.Module

class AnswerLocalDataSourceMock(
    private val currentAnswerToReturn: Answer? = null,
    private val previousAnswersToReturn: List<Answer> = listOf(),
    val getCurrentErrorMessage: String? = null,
    private val saveErrorMessage: String? = null,
    val updateErrorMessage: String? = null
): AnswerLocalDataManageable
{
    override fun getCurrent(): Answer
    {
        currentAnswerToReturn?.let { return it }

        throw AnswerNotFoundLocalDataException(message = getCurrentErrorMessage)
    }

    override fun getPrevious(): List<Answer> = previousAnswersToReturn

    override fun save(newAnswer: Answer): Answer
    {
        saveErrorMessage?.let { throw AnswerSaveFailedLocalDataException(message = it) }

        return newAnswer
    }

    override fun update(existingAnswer: Answer): Answer
    {
        updateErrorMessage?.let { throw AnswerUpdateFailedLocalDataException(message = it) }

        return existingAnswer
    }

    companion object
    {
        private val previousAnswers = listOf(
            Answer(word = Word(word = "PLACE"), isCurrent = false),
            Answer(word = Word(word = "SHAPE"), isCurrent = false),
            Answer(word = Word(word = "GLASS"), isCurrent = false),
            Answer(word = Word(word = "LOOKS"), isCurrent = false),
        )

        private val currentAnswers = listOf(
            Answer(word = Word(word = "MIGHT"), isCurrent = false),
            Answer(word = Word(word = "STRAY"), isCurrent = false),
            Answer(word = Word(word = "SLEEP"), isCurrent = false),
            Answer(word = Word(word = "CLOUT"), isCurrent = false),
        )

        fun injectDefinition(module: Module, scenarios: List<Scenario>)
        {
            val answerFailsToUpdate = scenarios.firstOrNull { it == Scenario.ANSWER_FAILS_TO_UPDATE } != null
            val answerWasSaved = scenarios.firstOrNull { it == Scenario.ANSWER_SAVED } != null
            val previousAnswersFound = scenarios.firstOrNull { it == Scenario.PREVIOUS_ANSWERS_FOUND } != null

            module.single<AnswerLocalDataManageable>()
            {
                AnswerLocalDataSourceMock(
                    currentAnswerToReturn = if (answerWasSaved) currentAnswers.random() else null,
                    previousAnswersToReturn = if (previousAnswersFound) previousAnswers else listOf(),
                    getCurrentErrorMessage = if (answerWasSaved) null else "Failed to get Answer.",
                    updateErrorMessage = if (answerFailsToUpdate) "Failed to update Answer." else null
                )
            }
        }
    }
}