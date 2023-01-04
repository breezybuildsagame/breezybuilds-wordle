package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

data class Announcement(private var message: String? = null)
{
    fun message() = this.message

    fun setMessage(newMessage: String?) { this.message = newMessage }
}
