package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class AnnouncementTests
{
    @Test
    fun `When initialized with a message - the message method returns the expected string value`()
    {
        // given
        val expectedMessage = "This is an important announcement!"

        // when
        val sut = Announcement(message = expectedMessage)

        // then
        assertEquals(expectedMessage, sut.message())
    }
}