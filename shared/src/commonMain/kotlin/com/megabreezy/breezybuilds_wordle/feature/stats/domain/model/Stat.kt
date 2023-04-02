package com.megabreezy.breezybuilds_wordle.feature.stats.domain.model

data class Stat(
    private val headline: String = "",
    private val description: String = ""
)
{
    fun headline() = this.headline

    fun description() = this.description
}
