package com.megabreezy.breezybuilds_wordle.core.data.source.word

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey

class CachedWord(): RealmObject
{
    @PrimaryKey
    @Index
    var _id: String = RealmUUID.random().toString()

    @Index
    var word: String = ""
}