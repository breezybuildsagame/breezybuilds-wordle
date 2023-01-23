package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

interface AnnouncementRepresentable
{
    fun message(): String?
    fun setMessage(newMessage: String?)
}