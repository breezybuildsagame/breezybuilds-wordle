package com.megabreezy.breezybuilds_wordle.core.data.source.answer

import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer

interface AnswerLocalDataManageable
{
    fun getCurrent(): Answer
    fun getPrevious(): List<Answer>
    suspend fun put(newAnswer: Answer): Answer
    fun update(existingAnswer: Answer): Answer
}

class AnswerNotFoundLocalDataException(message: String?): Exception(message)
class AnswerPutFailedLocalDataException(message: String?): Exception(message)
class AnswerUpdateFailedLocalDataException(message: String?): Exception(message)