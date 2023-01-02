package com.megabreezy.breezybuilds_wordle.feature.game.data.gateway

import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.answer.AnswerNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.data.source.word.WordNotFoundLocalDataException
import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
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
        val excludedWords = mutableListOf<Word>()

        try
        {
            val mostRecentAnswer = answerLocalDataSource.getCurrent()
            answerLocalDataSource.getPrevious().forEach { excludedWords.add(Word(word = it.word().toString())) }
            excludedWords.add(Word(word = mostRecentAnswer.word().toString()))

            val word = wordLocalDataSource.get(excludingWords = excludedWords)

            answerLocalDataSource.put(newAnswer = Answer(word = word, isCurrent = true))
            mostRecentAnswer.setIsCurrent(newIsCurrent = false)
            answerLocalDataSource.update(existingAnswer = mostRecentAnswer)

            return GameAnswer(word = word.toString())
        }
        catch(e: AnswerNotFoundLocalDataException)
        {
            answerLocalDataSource.getPrevious().forEach { excludedWords.add(Word(word = it.word().toString())) }

            val word = wordLocalDataSource.get(excludingWords = excludedWords)

            answerLocalDataSource.put(newAnswer = Answer(word = word, isCurrent = true))

            return GameAnswer(word = word.toString())
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

            return GameAnswer(word = answer.word().toString())
        }
        catch(e: AnswerNotFoundLocalDataException)
        {
            throw GameAnswerNotFoundRepositoryException(message = e.message)
        }
    }
}