package com.megabreezy.breezybuilds_wordle.feature.help.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.help.domain.model.HelpSheet
import kotlin.test.Test
import kotlin.test.assertEquals

class GetHelpSheetTests
{
    @Test
    fun `When use case invoked - expected HelpSheet is returned`()
    {
        // given
        val expectedHelpSheet = HelpSheet(
            title = "HOW TO PLAY",
            instructions = listOf(
                HelpSheet.Instruction(
                    instruction = "Guess the **WORDLE** in 6 tries."
                ),
                HelpSheet.Instruction(
                    instruction = "Each guess must be a valid 5 letter word. Hit the enter button to submit."
                ),
                HelpSheet.Instruction(
                    instruction = "After each guess, the color of the tiles will change to show how close your guess was to the word."
                )
            ),
            examples = listOf(
                HelpSheet.Example(
                    tiles = listOf(
                        HelpSheet.Tile(letter = 'W', state = HelpSheet.Tile.State.CORRECT),
                        HelpSheet.Tile(letter = 'E', state = HelpSheet.Tile.State.HIDDEN),
                        HelpSheet.Tile(letter = 'A', state = HelpSheet.Tile.State.HIDDEN),
                        HelpSheet.Tile(letter = 'R', state = HelpSheet.Tile.State.HIDDEN),
                        HelpSheet.Tile(letter = 'Y', state = HelpSheet.Tile.State.HIDDEN),
                    ),
                    description = "The letter **W** is in the word and in the correct spot."
                ),
                HelpSheet.Example(
                    tiles = listOf(
                        HelpSheet.Tile(letter = 'P', state = HelpSheet.Tile.State.HIDDEN),
                        HelpSheet.Tile(letter = 'I', state = HelpSheet.Tile.State.CLOSE),
                        HelpSheet.Tile(letter = 'L', state = HelpSheet.Tile.State.HIDDEN),
                        HelpSheet.Tile(letter = 'L', state = HelpSheet.Tile.State.HIDDEN),
                        HelpSheet.Tile(letter = 'S', state = HelpSheet.Tile.State.HIDDEN),
                    ),
                    description = "The letter **I** is in the word but in the wrong spot."
                ),
                HelpSheet.Example(
                    tiles = listOf(
                        HelpSheet.Tile(letter = 'V', state = HelpSheet.Tile.State.HIDDEN),
                        HelpSheet.Tile(letter = 'A', state = HelpSheet.Tile.State.CLOSE),
                        HelpSheet.Tile(letter = 'G', state = HelpSheet.Tile.State.HIDDEN),
                        HelpSheet.Tile(letter = 'U', state = HelpSheet.Tile.State.INCORRECT),
                        HelpSheet.Tile(letter = 'E', state = HelpSheet.Tile.State.HIDDEN),
                    ),
                    description = "The letter **U** is not in the word."
                )
            ),
            footer = "No daily restrictions. Play as often as you like!"
        )

        // when
        val actualSheet = HelpUseCase().getHelpSheet()

        // then
        assertEquals(expectedHelpSheet, actualSheet)
    }
}