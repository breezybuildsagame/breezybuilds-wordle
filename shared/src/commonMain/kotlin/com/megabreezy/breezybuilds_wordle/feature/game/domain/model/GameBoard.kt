package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

class GameBoard(private var rows: List<List<Tile>> = listOf())
{
    fun rows() = this.rows

    fun setRows(newRows: List<List<Tile>>) { this.rows = newRows }

    class Tile(
        private var letter: Char? = null,
        private var state: State = State.HIDDEN
    )
    {
        fun letter() = this.letter

        fun setLetter(newLetter: Char) { this.letter = newLetter }

        fun state(): State = this.state

        fun setState(newState: State) { this.state = newState }

        enum class State { HIDDEN, CLOSE, INCORRECT, CORRECT }
    }
}