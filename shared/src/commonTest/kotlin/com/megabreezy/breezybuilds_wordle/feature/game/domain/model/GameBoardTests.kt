package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class GameBoardTests
{
    val mockRows = listOf(
        listOf(GameBoard.Tile(letter = 't'), GameBoard.Tile(letter = 'e'), GameBoard.Tile(letter = 's'), GameBoard.Tile(letter = 't')),
        listOf(GameBoard.Tile(letter = 't')),
    )

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
        val expectedRows = mockRows

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

    @Test
    fun `when activeRow method invoked on new instance - first row is returned`()
    {
        // given
        val expectedActiveRow = mockRows.first()
        val sut = GameBoard(rows = mockRows)

        // when
        val actualActiveRow = sut.activeRow()

        // then
        assertEquals(expectedActiveRow, actualActiveRow)
    }

    @Test
    fun `when activeRow method invoked on instance that has had rows added later - first row is returned`()
    {
        // given
        val expectedActiveRow = mockRows.first()
        val sut = GameBoard()
        sut.setRows(newRows = mockRows)

        // when
        val actualActiveRow = sut.activeRow()

        // then
        assertEquals(expectedActiveRow, actualActiveRow)
    }

    @Test
    fun `when setNewActiveRow invoked on fresh instance - second row is returned`()
    {
        // given
        val expectedActiveRow = mockRows.last()
        val sut = GameBoard(rows = mockRows)

        // when
        sut.setNewActiveRow()
        val actualActiveRow = sut.activeRow()

        // then
        assertEquals(expectedActiveRow, actualActiveRow)
    }

    @Test
    fun `when setNewActiveRow invoked on instance with last row as active row - expected exception is thrown`()
    {
        // given
        val expectedExceptionMessage = "No more rows left to activate."
        val sut = GameBoard(rows = mockRows)
        sut.setNewActiveRow()

        // when
        val actualException = assertFailsWith<GameBoard.SetNewActiveRowFailedException>()
        {
            sut.setNewActiveRow()
        }

        // then
        assertEquals(expectedExceptionMessage, actualException.message)
    }
}