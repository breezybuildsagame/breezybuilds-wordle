package com.megabreezy.breezybuilds_wordle.core.data.source.answer.mock

import com.megabreezy.breezybuilds_wordle.core.data.source.answer.*
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word

class AnswerLocalDataSourceCommonMock: AnswerLocalDataManageable
{
    var getCurrentAnswerToReturn: Answer? = null
    var getCurrentAnswerShouldFail: Boolean = false

    var getPreviousAnswersToReturn = listOf<Answer>()

    var putNewAnswerToReturn: Answer? = null
    var putNewAnswerShouldFail: Boolean = false

    var updatedAnswerToReturn: Answer? = null
    var updateAnswerShouldFail: Boolean = false

    override fun getCurrent(): Answer
    {
        if (getCurrentAnswerShouldFail) throw AnswerNotFoundLocalDataException(message = "Answer not found.")

        getCurrentAnswerToReturn = Answer(word = Word(word = "SLAYS"), isCurrent = true)

        return getCurrentAnswerToReturn!!
    }

    override fun getPrevious(): List<Answer> = getPreviousAnswersToReturn

    override suspend fun insert(newAnswer: Answer): Answer
    {
        if (putNewAnswerShouldFail) throw AnswerInsertFailedLocalDataException(message = "New Answer not saved.")

        putNewAnswerToReturn = newAnswer

        return putNewAnswerToReturn!!
    }

    override suspend fun update(existingAnswer: Answer, updatedAnswer: Answer): Answer
    {
        if (updateAnswerShouldFail) throw AnswerUpdateFailedLocalDataException(message = "Existing Answer not updated.")

        updatedAnswerToReturn = updatedAnswer

        return updatedAnswerToReturn!!
    }
}