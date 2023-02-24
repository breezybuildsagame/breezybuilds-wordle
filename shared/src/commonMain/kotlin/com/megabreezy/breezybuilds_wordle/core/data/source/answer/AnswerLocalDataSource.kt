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
        val cachedAnswers = realm?.query<CachedAnswer>("isCurrent = true")?.find()

        Answer(word = Word(word = cachedAnswers!!.first().word))
    }
    catch (e: Throwable)
    {
        throw AnswerNotFoundLocalDataException("No current answer found.")
    }

    override fun getPrevious(): List<Answer>
    {
        TODO("Not yet implemented")
    }

    override fun put(newAnswer: Answer): Answer
    {
        TODO("Not yet implemented")
    }

    override fun update(existingAnswer: Answer): Answer
    {
        TODO("Not yet implemented")
    }
}