package com.megabreezy.breezybuilds_wordle.feature.stats.domain.model

data class GuessDistribution(
    private val title: String = "",
    private val rows: List<Row> = listOf()
)
{
    fun title() = this.title

    fun rows(): List<Row> = this.rows

    data class Row(
        private val round: Int = 0,
        private val correctGuessesCount: Int = 0
    )
    {
        fun round() = this.round

        fun correctGuessesCount() = this.correctGuessesCount
    }
}
