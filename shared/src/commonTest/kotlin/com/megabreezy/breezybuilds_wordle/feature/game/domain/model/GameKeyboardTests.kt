package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GameKeyboardTests
{
    @Test
    fun `when Key is initialized with letter char - letter method returns expected char value`()
    {
        // given
        val expectedCharValue = 'A'

        // when
        val sut = GameKeyboard.Key(letter = expectedCharValue)

        // then
        assertEquals(expectedCharValue, sut.letter())
    }

    @Test
    fun `when Key is initialized with resourceId string - resourceId method returns expected string value`()
    {
        // given
        val expectedResourceId = "custom_key_image_id"

        // when
        val sut = GameKeyboard.Key(resourceId = expectedResourceId)

        // then
        assertEquals(expectedResourceId, sut.resourceId())
    }

    @Test
    fun `when setOnClick is invoked on Key instance with function and Key is clicked - expected onClick function is invoked`()
    {
        // given
        var onClickDidInvoke = false
        val sut = GameKeyboard.Key()

        // when
        sut.setOnClick { onClickDidInvoke = true }
        sut.click()

        // then
        assertTrue(onClickDidInvoke)
    }

    @Test
    fun `when Key is initialized with isEnabled set to true - isEnabled function returns true`()
    {
        // when
        val sut = GameKeyboard.Key(isEnabled = true)

        // then
        assertTrue(sut.isEnabled())
    }

    @Test
    fun `when Key is initialized with isEnabled set to false - isEnabled function returns false`()
    {
        // when
        val sut = GameKeyboard.Key(isEnabled = false)

        // then
        assertFalse(sut.isEnabled())
    }

    @Test
    fun `when Key is initialized with backgroundColor - backgroundColor function returns expected enum value`()
    {
        // given
        val expectedBackgroundColor = GameKeyboard.Key.BackgroundColor.NEARBY

        // when
        val sut = GameKeyboard.Key(backgroundColor = expectedBackgroundColor)

        // then
        assertEquals(expectedBackgroundColor, sut.backgroundColor())
    }

    @Test
    fun `when setBackgroundColor invoked on Key instance with new color enum - backgroundColor function returns expected enum value`()
    {
        // given
        val sut = GameKeyboard.Key(backgroundColor = GameKeyboard.Key.BackgroundColor.NOT_FOUND)

        // when
        sut.setBackgroundColor(newBackgroundColor = GameKeyboard.Key.BackgroundColor.CORRECT)

        // then
        assertEquals(GameKeyboard.Key.BackgroundColor.CORRECT, sut.backgroundColor())
    }

    @Test
    fun `when setIsEnabled invoked on Key instance with new boolean value - isEnabled function returns expected boolean value`()
    {
        // given
        val sut = GameKeyboard.Key(isEnabled = true)

        // when
        sut.setIsEnabled(newIsEnabled = false)

        // then
        assertFalse(sut.isEnabled())
    }

    @Test
    fun `when initialized and rows method is invoked - expected list of keys is returned`()
    {
        // given
        val expectedRows = listOf(
            listOf(
                GameKeyboard.Key(letter = 'q'), GameKeyboard.Key(letter = 'w'), GameKeyboard.Key(letter = 'e'),
                GameKeyboard.Key(letter = 'r'), GameKeyboard.Key(letter = 't'), GameKeyboard.Key(letter = 'y'),
                GameKeyboard.Key(letter = 'u'), GameKeyboard.Key(letter = 'i'), GameKeyboard.Key(letter = 'o'), GameKeyboard.Key(letter = 'p')
            ),
            listOf(
                GameKeyboard.Key(letter = 'a'), GameKeyboard.Key(letter = 's'), GameKeyboard.Key(letter = 'd'),
                GameKeyboard.Key(letter = 'f'), GameKeyboard.Key(letter = 'g'), GameKeyboard.Key(letter = 'h'),
                GameKeyboard.Key(letter = 'j'), GameKeyboard.Key(letter = 'k'), GameKeyboard.Key(letter = 'l')
            ),
            listOf(
                GameKeyboard.Key(letters = "ENTER"), GameKeyboard.Key(letter = 'z'), GameKeyboard.Key(letter = 'x'),
                GameKeyboard.Key(letter = 'c'), GameKeyboard.Key(letter = 'v'), GameKeyboard.Key(letter = 'b'),
                GameKeyboard.Key(letter = 'n'), GameKeyboard.Key(letter = 'm'), GameKeyboard.Key(letters = "BACKSPACE", resourceId = "game_image_backspace")
            )
        )
        
        // when
        val sut = GameKeyboard()
        
        // then
        assertEquals(expectedRows, sut.rows())
    }
}