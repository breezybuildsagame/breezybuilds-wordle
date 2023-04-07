package com.megabreezy.breezybuilds_wordle.core.data.source.completed_game

import com.megabreezy.breezybuilds_wordle.core.domain.model.CompletedGame
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query

class CompletedGameLocalDataSource(private var realm: Realm? = null): CompletedGameLocalDataManageable
{
    init
    {
        val config = RealmConfiguration.create(schema = setOf(CachedCompletedGame::class))
        realm = realm ?: Realm.open(configuration = config)
    }

    override fun getAll(): List<CompletedGame>
    {
        return realm?.query<CachedCompletedGame>()?.find()
            ?.map { CompletedGame(answer = it.answer(), date = it.date, playerGuesses = it.playerGuesses()) } ?: listOf()
    }

    override suspend fun put(newCompletedGame: CompletedGame): CompletedGame
    {
        realm?.write()
        {
            copyToRealm(
                CachedCompletedGame().apply()
                {
                    answer = newCompletedGame.answer()
                    date = newCompletedGame.date()
                    playerGuesses = newCompletedGame.playerGuesses().map { "${it.word()}" }
                    playerGuessedCorrectly = newCompletedGame.answer().playerGuessedCorrectly() ?: false
                    word = "${newCompletedGame.answer().word()}"
                }
            )
        }

        return newCompletedGame
    }
}