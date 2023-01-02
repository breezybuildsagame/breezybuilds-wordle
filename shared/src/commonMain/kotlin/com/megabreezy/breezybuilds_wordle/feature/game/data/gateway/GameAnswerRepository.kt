package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway

import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerNotFoundRepositoryException
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameAnswer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GameAnswerRepository: GameAnswerGateway, KoinComponent
{
    private val answerLocalDataSource: AnswerLocalDataManageable by inject()
    private val wordLocalDataSource: WordLocalDataManageable by inject()

    override fun create(): GameAnswer
    {
        try
        {
            val word = wordLocalDataSource.get()
            answerLocalDataSource.put(newAnswer = Answer(word = word, isCurrent = true))
            return GameAnswer(word = word.word())
        }
        catch(e: WordNotFoundLocalDataException)
        {
            throw GameAnswerNotFoundRepositoryException(message = e.message)
        }
    }

    override fun get(): GameAnswer
    {
        try
        {
            val answer = answerLocalDataSource.getCurrent()

            return GameAnswer(word = answer.word().word())
        }
        catch(e: AnswerNotFoundLocalDataException)
        {
            throw GameAnswerNotFoundRepositoryException(message = e.message)
        }
    }
}