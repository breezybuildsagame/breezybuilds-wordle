package com.megabreezy.breezybuilds_wordle.core.data.source.guess

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey

class CachedGuess(): RealmObject
{
    @PrimaryKey
    var _id: String = RealmUUID.random().toString()

    @Index
    var word: String = ""
}