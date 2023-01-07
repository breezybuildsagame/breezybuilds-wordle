package com.megabreezy.breezybuilds_wordle.feature.game.domain.model

class GameHeader(
    private val title: String = "",
    private val options: List<Option> = listOf()
)
{
    fun title() = this.title

    fun options() = this.options

    data class Option(
        private val iconResourceId: String? = null,
        private var onClick: () -> Unit = { }
    )
    {
        override fun equals(other: Any?): Boolean = other is Option
            && this.iconResourceId() == other.iconResourceId()

        override fun hashCode(): Int = iconResourceId?.hashCode() ?: (1..1000).random()

        fun click() = onClick()

        fun iconResourceId(): String? = this.iconResourceId

        fun setOnClick(newOnClick: () -> Unit) { onClick = newOnClick }
    }
}