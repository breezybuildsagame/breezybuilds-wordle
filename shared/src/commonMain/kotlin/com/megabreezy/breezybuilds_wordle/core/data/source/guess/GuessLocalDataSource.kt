package com.megabreezy.breezybuilds_wordle.core.data.source.guess

import com.megabreezy.breezybuilds_wordle.core.domain.model.Guess
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class GuessLocalDataSource(private var realm: Realm? = null): GuessLocalDataManageable
{
    init
    {
        val config = RealmConfiguration.create(schema = setOf(CachedGuess::class))
        realm = realm ?: Realm.open(configuration = config)
    }

    override fun getAll(): List<Guess>
    {
        TODO("Not yet implemented")
    }

    override fun save(newGuess: String): Guess
    {
        TODO("Not yet implemented")
    }

    override fun clear()
    {
        TODO("Not yet implemented")
    }
}