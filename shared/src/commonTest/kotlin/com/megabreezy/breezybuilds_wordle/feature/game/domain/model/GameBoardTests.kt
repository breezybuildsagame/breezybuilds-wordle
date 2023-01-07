package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class GameBoardTests
{
    val mockRows = listOf(
        listOf(GameBoard.Tile(letter = 't'), GameBoard.Tile(letter = 'e'), GameBoard.Tile(letter = 's'), GameBoard.Tile(letter = 't')),
        listOf(GameBoard.Tile(letter = 't')),
    )

    @Test
    fun `When Tile is initialized with letter - letter method returns expected char value`()
    {
        // given
        val expectedLetter = 'z'

        // When
        val sut = GameBoard.Tile(letter = expectedLetter)

        // then
        assertEquals(expectedLetter, sut.letter())
    }

    @Test
    fun `When Tile is initialized with state enum - state method returns expected state value`()
    {
        // given
        val expectedState = GameBoard.Tile.State.CLOSE

        // When
        val sut = GameBoard.Tile(state = expectedState)

        // then
        assertEquals(expectedState, sut.state())
    }

    @Test
    fun `When Tile setLetter method invoked with new letter - letter method returns expected value`()
    {
        // given
        val expectedLetter = 'L'

        // When
        val sut = GameBoard.Tile(letter = 'M')
        sut.setLetter(newLetter = expectedLetter)

        // then
        assertEquals(expectedLetter, sut.letter())
    }

    @Test
    fun `When Tile setState method is invoked with new state - state method returns expected state value`()
    {
        // given
        val expectedState = GameBoard.Tile.State.CORRECT

        // When
        val sut = GameBoard.Tile(state = GameBoard.Tile.State.CLOSE)
        sut.setState(newState = expectedState)

        // then
        assertEquals(expectedState, sut.state())
    }

    @Test
    fun `When initialized with list of Tile entities - rows method returns expected list`()
    {
        // given
        val expectedRows = mockRows

        // When
        val sut = GameBoard(rows = expectedRows)

        // then
        assertEquals(expectedRows, sut.rows())
    }

    @Test
    fun `When setRows method invoked with list of tiles - rows method returns expected list`()
    {
        // given
        val expectedRows = listOf(
            listOf(GameBoard.Tile(letter = 't'), GameBoard.Tile(letter = 'e'), GameBoard.Tile(letter = 's'), GameBoard.Tile(letter = 't')),
            listOf(GameBoard.Tile(letter = 't')),
        )

        // When
        val sut = GameBoard()
        sut.setRows(newRows = expectedRows)

        // then
        assertEquals(expectedRows, sut.rows())
    }

    @Test
    fun `When activeRow method invoked on new instance - first row is returned`()
    {
        // given
        val expectedActiveRow = mockRows.first()
        val sut = GameBoard(rows = mockRows)

        // When
        val actualActiveRow = sut.activeRow()

        // then
        assertEquals(expectedActiveRow, actualActiveRow)
    }

    @Test
    fun `When activeRow method invoked on instance that has had rows added later - first row is returned`()
    {
        // given
        val expectedActiveRow = mockRows.first()
        val sut = GameBoard()
        sut.setRows(newRows = mockRows)

        // When
        val actualActiveRow = sut.activeRow()

        // then
        assertEquals(expectedActiveRow, actualActiveRow)
    }

    @Test
    fun `When setNewActiveRow invoked on fresh instance - second row is returned`()
    {
        // given
        val expectedActiveRow = mockRows.last()
        val sut = GameBoard(rows = mockRows)

        // When
        sut.setNewActiveRow()
        val actualActiveRow = sut.activeRow()

        // then
        assertEquals(expectedActiveRow, actualActiveRow)
    }

    @Test
    fun `When setNewActiveRow invoked on instance with last row as active row - expected exception is thrown`()
    {
        // given
        val expectedExceptionMessage = "No more rows left to activate."
        val sut = GameBoard(rows = mockRows)
        sut.setNewActiveRow()

        // When
        val actualException = assertFailsWith<GameBoard.SetNewActiveRowFailedException>()
        {
            sut.setNewActiveRow()
        }

        // then
        assertEquals(expectedExceptionMessage, actualException.message)
    }
    
    @Test
    fun `When reset method invoked on instance - all tile letters are cleared`()
    {
        // given
        val sut = GameBoard(rows = mockRows)
        for (row in sut.rows()) { for (tile in row) { tile.setLetter(newLetter = 'B') } }

        // when
        sut.reset()

        // then
        for (row in sut.rows()) { for (tile in row) { assertNull(tile.letter()) } }
    }

    @Test
    fun `When reset method invoked on instance - all tile states are reset to HIDDEN`()
    {
        // given
        val sut = GameBoard(rows = mockRows)
        for (row in sut.rows()) { for (tile in row) { tile.setState(newState = GameBoard.Tile.State.CLOSE) } }

        // when
        sut.reset()

        // then
        for (row in sut.rows()) { for (tile in row) { assertEquals(GameBoard.Tile.State.HIDDEN, tile.state()) } }
    }

    @Test
    fun `When reset method invoked on instance - active row is reset to first row of tiles`()
    {
        // given
        val sut = GameBoard(rows = mockRows)
        sut.setNewActiveRow()

        // when
        sut.reset()

        // then
        assertEquals(sut.rows().first(), sut.activeRow())
    }
}