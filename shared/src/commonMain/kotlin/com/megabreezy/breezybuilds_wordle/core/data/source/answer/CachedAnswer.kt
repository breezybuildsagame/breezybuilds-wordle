package com.megabreezy.breezybuilds_wordle.core.data.source.answer

import com.megabreezy.breezybuilds_wordle.core.domain.model.Answer
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.Index
import io.realm.kotlin.types.annotations.PrimaryKey

class CachedAnswer(): RealmObject {
    @PrimaryKey
    var _id: String = RealmUUID.random().toString()

    @Index
    var word: String = ""
    var isCurrent: Boolean = false

    constructor(answer: Answer): this()
    {
        this.word = answer.word().toString()
        this.isCurrent = answer.isCurrent()
    }
}