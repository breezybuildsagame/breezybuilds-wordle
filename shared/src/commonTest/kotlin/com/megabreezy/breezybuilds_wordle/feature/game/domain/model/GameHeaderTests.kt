package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GameHeaderTests
{
    @Test
    fun `When option initialized with iconResourceId - iconResourceId method returns expected string value`()
    {
        // given
        val expectedResourceId = "settings_image_option"

        // when
        val sut = GameHeader.Option(iconResourceId = expectedResourceId)

        // then
        assertEquals(expectedResourceId, sut.iconResourceId())
    }

    @Test
    fun `When option setOnClick method invoked with new function and option is clicked - expected onClick method is invoked`()
    {
        // given
        var onClickDidInvoke = false
        val sut = GameHeader.Option()

        // when
        sut.setOnClick { onClickDidInvoke = true }
        sut.click()

        // then
        assertTrue(onClickDidInvoke)
    }

    @Test
    fun `When initialized with title - title method returns expected string value`()
    {
        // given
        val expectedTitle = "BreezyBuildsWordle!"

        // when
        val sut = GameHeader(title = expectedTitle)

        // then
        assertEquals(expectedTitle, sut.title())
    }

    @Test
    fun `When initialized with options - options method returns expected list of options`()
    {
        // given
        val expectedOptions = listOf(
            GameHeader.Option(iconResourceId = "settings_image_option"),
            GameHeader.Option(iconResourceId = "stats_image_option")
        )

        // when
        val sut = GameHeader(options = expectedOptions)

        // then
        assertEquals(expectedOptions, sut.options())
    }
}