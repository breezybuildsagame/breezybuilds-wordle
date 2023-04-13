package com.megabreezy.breezybuilds_wordle.core.data.source.completed_game

import com.megabreezy.breezybuilds_wordle.core.domain.model.CompletedGame
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf

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
        val guessesToAdd = realmListOf<String>()
        newCompletedGame.playerGuesses().forEach { guessesToAdd.add("${it.word()}") }

        realm?.write()
        {
            copyToRealm(
                CachedCompletedGame().apply()
                {
                    answer = newCompletedGame.answer()
                    date = newCompletedGame.date()
                    playerGuesses = guessesToAdd
                    playerGuessedCorrectly = newCompletedGame.answer().playerGuessedCorrectly() ?: false
                    word = "${newCompletedGame.answer().word()}"
                }
            )
        }

        return newCompletedGame
    }
}