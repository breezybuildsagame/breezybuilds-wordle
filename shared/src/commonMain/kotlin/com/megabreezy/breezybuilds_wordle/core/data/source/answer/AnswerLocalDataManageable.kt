package com.megabreezy.breezybuilds_wordle.core.data.source.answer

import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer

interface AnswerLocalDataManageable
{
    fun getCurrent(): Answer
    fun getPrevious(): List<Answer>
    suspend fun insert(newAnswer: Answer): Answer
    suspend fun update(existingAnswer: Answer, updatedAnswer: Answer): Answer
}

class AnswerNotFoundLocalDataException(message: String?): Exception(message)
class AnswerInsertFailedLocalDataException(message: String?): Exception(message)
class AnswerUpdateFailedLocalDataException(message: String?): Exception(message)