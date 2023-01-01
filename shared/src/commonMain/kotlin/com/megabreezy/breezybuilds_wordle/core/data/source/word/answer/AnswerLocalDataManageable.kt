package com.megabreezy.breezybuilds_wordle.core.data.source.word.answer

import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer

interface AnswerLocalDataManageable
{
    fun getCurrent(): Answer
    fun getPrevious(): List<Answer>
    fun save(): Answer
    fun update(existingAnswer: Answer): Answer
}

class AnswerNotFoundLocalDataException(message: String?): Exception(message)
class AnswerSaveFailedLocalDataException(message: String?): Exception(message)
class AnswerUpdateFailedLocalDataException(message: String?): Exception(message)