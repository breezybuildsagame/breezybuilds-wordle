package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

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

    @Test
    fun `When setMessage method invoked with new message on instance - the message method returns the expected string value`()
    {
        // given
        val expectedUpdatedMessage = "Another important announcement!"
        val sut = Announcement()

        // when
        sut.setMessage(newMessage = expectedUpdatedMessage)

        // then
        assertEquals(expectedUpdatedMessage, sut.message())
    }

    @Test
    fun `When setMessage method invoked with null value on instance - the message method returns expected null value`()
    {
        // given
        val sut = Announcement(message = "My awesome message!")

        // when
        sut.setMessage(newMessage = null)

        // then
        assertNull(sut.message())
    }
}