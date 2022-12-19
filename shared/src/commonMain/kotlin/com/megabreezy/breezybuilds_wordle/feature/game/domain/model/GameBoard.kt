package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

class GameBoard(private val rows: List<List<Tile>> = listOf())
{
    fun rows() = this.rows

    class Tile(
        private val letter: Char? = null,
        private val state: State = State.HIDDEN
    )
    {
        fun letter() = this.letter

        fun state(): State = this.state

        enum class State { HIDDEN, CLOSE, INCORRECT, CORRECT }
    }
}