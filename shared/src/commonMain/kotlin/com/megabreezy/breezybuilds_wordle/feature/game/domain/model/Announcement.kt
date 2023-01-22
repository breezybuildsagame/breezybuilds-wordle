package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

data class Announcement(private var message: String? = null): AnnouncementRepresentable
{
    override fun message() = this.message

    override fun setMessage(newMessage: String?) { this.message = newMessage }
}
