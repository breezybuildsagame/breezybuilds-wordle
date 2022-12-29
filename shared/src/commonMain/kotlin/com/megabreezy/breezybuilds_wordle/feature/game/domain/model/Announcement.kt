package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

data class Announcement(private val message: String)
{
    fun message() = this.message
}
