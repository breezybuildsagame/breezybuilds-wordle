package com.megabreezy.breezybuilds_wordle.feature.help.domain.model

data class HelpSheet(
    private val title: String = "",
    private val instructions: List<Instruction> = listOf(),
    private val examples: List<Example> = listOf(),
    private val footer: String = ""
)
{
    fun title() = this.title
    fun instructions() = this.instructions
    fun examples() = this.examples
    fun footer() = this.footer

    data class Example(
        private val description: String = "",
        private val tiles: List<HelpSheet.Tile> = listOf()
    )
    {
        fun description() = this.description
        fun tiles() = this.tiles
    }

    data class Instruction(
        private val instruction: String = ""
    )
    {
        fun instruction() = this.instruction
    }

    data class Tile(
        private val letter: Char = 'Z',
        private val state: State = State.HIDDEN
    )
    {
        fun letter() = this.letter
        fun state() = this.state

        enum class State { HIDDEN, CLOSE, INCORRECT, CORRECT }
    }
}