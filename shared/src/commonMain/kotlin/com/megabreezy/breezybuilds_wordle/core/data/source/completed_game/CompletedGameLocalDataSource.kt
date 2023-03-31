package com.megabreezy.breezybuilds_wordle.core.data.source.completed_game

import com.megabreezy.breezybuilds_wordle.core.domain.model.CompletedGame
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class CompletedGameLocalDataSource(private var realm: Realm? = null): CompletedGameLocalDataManageable
{
    init
    {
        val config = RealmConfiguration.create(schema = setOf(CachedCompletedGame::class))
        realm = realm ?: Realm.open(configuration = config)
    }

    override fun getAll(): List<CompletedGame>
    {
        TODO("Not yet implemented")
    }

    override suspend fun put(newCompletedGame: CompletedGame): CompletedGame
    {
        TODO("Not yet implemented")
    }
}