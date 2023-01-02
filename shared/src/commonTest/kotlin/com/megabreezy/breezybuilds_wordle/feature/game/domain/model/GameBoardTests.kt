package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals

class GameBoardTests
{
    @Test
    fun `when Tile is initialized with letter - letter method returns expected char value`()
    {
        // given
        val expectedLetter = 'z'

        // when
        val sut = GameBoard.Tile(letter = expectedLetter)

        // then
        assertEquals(expectedLetter, sut.letter())
    }

    @Test
    fun `when Tile is initialized with state enum - state method returns expected state value`()
    {
        // given
        val expectedState = GameBoard.Tile.State.CLOSE

        // when
        val sut = GameBoard.Tile(state = expectedState)

        // then
        assertEquals(expectedState, sut.state())
    }

    @Test
    fun `when Tile setLetter method invoked with new letter - letter method returns expected value`()
    {
        // given
        val expectedLetter = 'L'

        // when
        val sut = GameBoard.Tile(letter = 'M')
        sut.setLetter(newLetter = expectedLetter)

        // then
        assertEquals(expectedLetter, sut.letter())
    }

    @Test
    fun `when Tile setState method is invoked with new state - state method returns expected state value`()
    {
        // given
        val expectedState = GameBoard.Tile.State.CORRECT

        // when
        val sut = GameBoard.Tile(state = GameBoard.Tile.State.CLOSE)
        sut.setState(newState = expectedState)

        // then
        assertEquals(expectedState, sut.state())
    }

    @Test
    fun `when initialized with list of Tile entities - rows method returns expected list`()
    {
        // given
        val expectedRows = listOf(
            listOf(GameBoard.Tile(letter = 't'), GameBoard.Tile(letter = 'e'), GameBoard.Tile(letter = 's'), GameBoard.Tile(letter = 't')),
            listOf(GameBoard.Tile(letter = 't')),
        )

        // when
        val sut = GameBoard(rows = expectedRows)

        // then
        assertEquals(expectedRows, sut.rows())
    }

    @Test
    fun `when setRows method invoked with list of tiles - rows method returns expected list`()
    {
        // given
        val expectedRows = listOf(
            listOf(GameBoard.Tile(letter = 't'), GameBoard.Tile(letter = 'e'), GameBoard.Tile(letter = 's'), GameBoard.Tile(letter = 't')),
            listOf(GameBoard.Tile(letter = 't')),
        )

        // when
        val sut = GameBoard()
        sut.setRows(newRows = expectedRows)

        // then
        assertEquals(expectedRows, sut.rows())
    }
}