package com.megabreezy.breezybuilds_wordle.feature.help.domain.model

import com.megabreezy.breezybuilds_wordle.feature.help.domain.model.HelpSheet
import kotlin.test.Test
import kotlin.test.assertEquals

class HelpSheetTests
{
    @Test
    fun `When Tile initialized with state parameter - the state method returns expected state value`()
    {
        // given
        val expectedState = HelpSheet.Tile.State.CLOSE

        // when
        val sut = HelpSheet.Tile(state = HelpSheet.Tile.State.CLOSE)

        // then
        assertEquals(expectedState, sut.state())
    }

    @Test
    fun `When Tile initialized with letter char parameter - the letter method returns expected state value`()
    {
        // given
        val expectedLetter = 'T'

        // when
        val sut = HelpSheet.Tile(letter = expectedLetter)

        // then
        assertEquals(expectedLetter, sut.letter())
    }

    @Test
    fun `When Example initialized with tiles list - the tiles method returns expected list`()
    {
        // given
        val expectedTiles = listOf(
            HelpSheet.Tile(letter = 'Q'),
            HelpSheet.Tile(letter = 'Z', state = HelpSheet.Tile.State.CORRECT)
        )

        // when
        val sut = HelpSheet.Example(tiles = expectedTiles)

        // then
        assertEquals(expectedTiles, sut.tiles())
        expectedTiles.forEachIndexed()
        { index, tile ->
            assertEquals(expectedTiles[index].letter().toString(), "$tile")
        }
    }

    @Test
    fun `When Example initialized with description string parameter - the description method returns expected string value`()
    {
        // given
        val expectedDescription = "Test description."

        // when
        val sut = HelpSheet.Example(description = expectedDescription)

        // then
        assertEquals(expectedDescription, sut.description())
    }

    @Test
    fun `When Instruction initialized with string parameter - the instruction method returns expected string value`()
    {
        // given
        val expectedInstruction = "This is a custom instruction for testing purposes."

        // when
        val sut = HelpSheet.Instruction(instruction = expectedInstruction)

        // then
        assertEquals(expectedInstruction, sut.instruction())
    }

    @Test
    fun `When HelpSheet initialized with title - the title method returns expected string value`()
    {
        // given
        val expectedTitle = "My Custom Help Sheet"

        // when
        val sut = HelpSheet(title = expectedTitle)

        // then
        assertEquals(expectedTitle, sut.title())
    }

    @Test
    fun `When HelpSheet initialized with instructions list parameter - the instructions method returns expected list`()
    {
        // given
        val expectedInstructions = listOf(
            HelpSheet.Instruction(instruction = "Test instruction"),
            HelpSheet.Instruction(instruction = "And another one!")
        )

        // when
        val sut = HelpSheet(instructions = expectedInstructions)

        // then
        assertEquals(expectedInstructions, sut.instructions())
    }

    @Test
    fun `When HelpSheet initialized with examples list parameter - the examples method returns expected list`()
    {
        // given
        val expectedExamples = listOf(
            HelpSheet.Example(description = "Tester"),
            HelpSheet.Example(tiles = listOf(HelpSheet.Tile(letter = 'X')))
        )

        // when
        val sut = HelpSheet(examples = expectedExamples)

        // then
        assertEquals(expectedExamples, sut.examples())
    }

    @Test
    fun `When HelpSheet initialized with footer string parameter - the footer method returns expected string value`()
    {
        // given
        val expectedFooter = "And that's all folks!"

        // when
        val sut = HelpSheet(footer = expectedFooter)

        // then
        assertEquals(expectedFooter, sut.footer())
    }
}