package com.megabreezy.breezybuilds_wordle.core.data.source.guess

import com.megabreezy.breezybuilds_wordle.core.domain.model.Guess
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class GuessLocalDataSource(private var realm: Realm? = null): GuessLocalDataManageable
{
    init
    {
        val config = RealmConfiguration.create(schema = setOf(CachedGuess::class))
        realm = realm ?: Realm.open(configuration = config)
    }

    override fun getAll(): List<Guess>
    {
        return realm?.query<CachedGuess>()?.find()?.map { Guess(word = Word(word = it.word)) } ?: listOf()
    }

    override suspend fun create(newGuess: String): Guess
    {
        var createdGuess: Guess? = null

        realm?.write()
        {
            if (query<CachedGuess>("word == '$newGuess'").find().isNotEmpty())
            {
                this.cancelWrite()
                throw GuessSaveFailedLocalDataException("Guess $newGuess has been previously guessed.")
            }

            copyToRealm(CachedGuess()).apply { word = newGuess }

            createdGuess = Guess(word = Word(word = newGuess))
        }

        createdGuess?.let { return it } ?: throw GuessSaveFailedLocalDataException("Unexpected error occurred.")
    }

    override suspend fun clear()
    {
        realm?.write()
        {
            val currentlyCachedGuesses = query<CachedGuess>().find()
            delete(currentlyCachedGuesses)
        }
    }
}