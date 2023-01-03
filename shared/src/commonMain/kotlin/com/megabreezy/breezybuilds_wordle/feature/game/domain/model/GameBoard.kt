package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

class GameBoard(private var rows: List<List<Tile>> = listOf())
{
    private var activeRow = if (rows.isNotEmpty()) 0 else null

    fun activeRow(): List<Tile>? = activeRow?.let { rows[it] }

    fun setNewActiveRow()
    {
        activeRow?.let { if (it >= rows.lastIndex) throw SetNewActiveRowFailedException("No more rows left to activate.") }

        activeRow = activeRow?.plus(1)
    }

    fun rows() = this.rows

    fun setRows(newRows: List<List<Tile>>)
    {
        this.rows = newRows

        activeRow = if (rows.isNotEmpty()) 0 else null
    }

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

    class SetNewActiveRowFailedException(message: String?): Exception(message)
}