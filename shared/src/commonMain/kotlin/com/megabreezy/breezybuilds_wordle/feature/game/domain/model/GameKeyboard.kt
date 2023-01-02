package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

class GameKeyboard
{
    private val rows: List<List<Key>> = listOf(
        listOf(
            Key(letter = 'q'), Key(letter = 'w'), Key(letter = 'e'), Key(letter = 'r'), Key(letter = 't'),
            Key(letter = 'y'), Key(letter = 'u'), Key(letter = 'i'), Key(letter = 'o'), Key(letter = 'p')
        ),
        listOf(
            Key(letter = 'a'), Key(letter = 's'), Key(letter = 'd'),
            Key(letter = 'f'), Key(letter = 'g'), Key(letter = 'h'),
            Key(letter = 'j'), Key(letter = 'k'), Key(letter = 'l')
        ),
        listOf(
            Key(letters = "ENTER"), Key(letter = 'z'), Key(letter = 'x'),
            Key(letter = 'c'), Key(letter = 'v'), Key(letter = 'b'),
            Key(letter = 'n'), Key(letter = 'm'), Key(letters = "BACKSPACE", resourceId = "game_image_backspace")
        )
    )

    fun rows(): List<List<Key>> = this.rows

    data class Key(
        private var backgroundColor: BackgroundColor = BackgroundColor.DEFAULT,
        private var isEnabled: Boolean = true,
        private val letter: Char? = null,
        private val letters: String? = null,
        private val resourceId: String? = null,
        private var onClick: () -> Unit = { }
    )
    {
        fun backgroundColor() = backgroundColor

        fun click() = onClick()

        fun isEnabled() = isEnabled

        fun letter(): Char? = letter

        fun letters() = if (letters.isNullOrEmpty()) letter.toString() else letters

        fun resourceId(): String? = resourceId

        fun setBackgroundColor(newBackgroundColor: BackgroundColor) { backgroundColor = newBackgroundColor }

        fun setIsEnabled(newIsEnabled: Boolean) { isEnabled = newIsEnabled }

        fun setOnClick(newOnClick: () -> Unit)
        {
            onClick = newOnClick
        }

        enum class BackgroundColor { DEFAULT, NOT_FOUND, NEARBY, CORRECT }
    }
}