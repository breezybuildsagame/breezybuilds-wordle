package com.megabreezy.breezybuilds_wordle.core.data.source.answer

import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class AnswerLocalDataSource(private var realm: Realm? = null): AnswerLocalDataManageable
{
    init
    {
        val config = RealmConfiguration.create(schema = setOf(CachedAnswer::class))
        realm = realm ?: Realm.open(configuration = config)
    }

    override fun getCurrent(): Answer = try
    {
        realm?.query<CachedAnswer>("isCurrent == true")?.find()?.map { Answer(word = Word(word = it.word)) }!!.last()
    }
    catch (e: Throwable)
    {
        throw AnswerNotFoundLocalDataException("No current answer found.")
    }

    override fun getPrevious(): List<Answer> = realm?.query<CachedAnswer>("isCurrent == false")?.find()?.map()
    { Answer(word = Word(it.word), isCurrent = it.isCurrent) }
    ?: listOf()

    override suspend fun insert(newAnswer: Answer): Answer
    {
        realm?.write()
        {
            if (this.query<CachedAnswer>("word == '${newAnswer.word()}'").find().isNotEmpty())
            {
                this.cancelWrite()
                throw AnswerInsertFailedLocalDataException("Answer: ${newAnswer.word()} already exists!")
            }

            this.query<CachedAnswer>("isCurrent == true").find().forEach { it.isCurrent = false }

            copyToRealm(CachedAnswer()).apply()
            {
                word = newAnswer.word().word()
                isCurrent = newAnswer.isCurrent()
            }
        }

        return newAnswer
    }

    override suspend fun update(existingAnswer: Answer, updatedAnswer: Answer): Answer
    {
        var successfullyUpdatedAnswer: Answer? = null

        realm?.write()
        {
            val cachedAnswer = this.query<CachedAnswer>("word == '${existingAnswer.word()}'").find().firstOrNull()

            cachedAnswer?.let()
            {
                it.word = existingAnswer.word().toString()
                it.isCurrent = existingAnswer.isCurrent()

                successfullyUpdatedAnswer = Answer(word = Word(word = it.word), isCurrent = it.isCurrent)
            }
        }

        successfullyUpdatedAnswer?.let { return it }

        throw AnswerUpdateFailedLocalDataException("Answer: ${existingAnswer.word()} not found! Try insert first.")
    }
}