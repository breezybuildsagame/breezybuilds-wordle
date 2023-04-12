package com.megabreezy.breezybuilds_wordle.core.data.source.completed_game

import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.CompletedGame
import com.megabreezy.breezybuilds_wordle.core.domain.model.Guess
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey

class CachedCompletedGame(): RealmObject
{
    @PrimaryKey
    var _id: String = RealmUUID.random().toString()

    @Index
    var word: String = ""
    @Index
    var date: Long = 0L

    var answer: Answer = Answer(word = Word(word = this.word), isCurrent = false)
    var isCurrent = false
    var playerGuessedCorrectly = false
    var playerGuesses: RealmList<String> = realmListOf()

    constructor(game: CompletedGame): this()
    {
        val guessesList = realmListOf<String>()
        game.playerGuesses().forEach { guessesList.add("${it.word()}") }

        this.answer = game.answer()
        this.date = game.date()
        this.playerGuesses = guessesList
        this.playerGuessedCorrectly = game.answer().playerGuessedCorrectly() ?: false
        this.isCurrent = game.answer().isCurrent()
        this.word = game.answer().word().toString()
    }

    fun answer() = Answer(
        isCurrent = this.isCurrent,
        playerGuessedCorrectly = this.playerGuessedCorrectly,
        word = Word(this.word)
    )

    fun playerGuesses(): List<Guess> = playerGuesses.map { Guess(word = Word(it)) }
}