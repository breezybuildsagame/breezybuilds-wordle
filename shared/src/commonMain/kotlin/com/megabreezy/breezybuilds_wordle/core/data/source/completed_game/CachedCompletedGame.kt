package com.megabreezy.breezybuilds_wordle.core.data.source.completed_game

import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import com.megabreezy.breezybuilds_wordle.core.domain.model.CompletedGame
import com.megabreezy.breezybuilds_wordle.core.domain.model.Guess
import com.megabreezy.breezybuilds_wordle.core.domain.model.Word
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
    var playerGuesses: List<Guess> = listOf()

    constructor(game: CompletedGame): this()
    {
        this.answer = game.answer()
        this.word = game.answer().word().toString()
        this.date = game.date()
        this.playerGuesses = game.playerGuesses()
    }
}