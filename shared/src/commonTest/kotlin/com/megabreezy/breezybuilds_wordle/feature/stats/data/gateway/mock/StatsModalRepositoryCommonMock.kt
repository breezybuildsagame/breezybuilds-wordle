package com.megabreezy.breezybuilds_wordle.feature.stats.data.gateway.mock

import com.megabreezy.breezybuilds_wordle.feature.stats.domain.gateway.StatsModalGateway
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.GuessDistribution
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.Stat
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.StatsModal

class StatsModalRepositoryCommonMock: StatsModalGateway
{
    var getStatsModalToReturn: StatsModal? = null
    var statsModalCloseButton: StatsModal.Button? = null

    override fun get(): StatsModal
    {
        getStatsModalToReturn?.let { return it }

        val modalToReturn = StatsModal(
            stats = listOf(Stat(headline = "2", description = "Win Streak")),
            guessDistribution = GuessDistribution(
                title = "Guess Distribution",
                rows = listOf(
                    GuessDistribution.Row(round = 1, correctGuessesCount = 2)
                )
            )
        )

        statsModalCloseButton?.let { modalToReturn.setCloseButton(newCloseButton = it) }
        getStatsModalToReturn = modalToReturn
        return modalToReturn
    }
}