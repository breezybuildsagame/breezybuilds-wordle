package com.megabreezy.breezybuilds_wordle.feature.stats.data.gateway

import com.megabreezy.breezybuilds_wordle.core.data.source.completed_game.CompletedGameLocalDataManageable
import com.megabreezy.breezybuilds_wordle.core.domain.model.CompletedGame
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.gateway.StatsModalGateway
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.GuessDistribution
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.Stat
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.StatsModal
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class StatsModalRepository: StatsModalGateway, KoinComponent
{
    private val localDataSource: CompletedGameLocalDataManageable by inject()

    override fun get(): StatsModal
    {
        val completedGames = localDataSource.getAll()
        val winGroups = getWinGroups(completedGames)

        val totalGamesPlayedStat = Stat(headline = "${completedGames.count()}", description = "Played")
        val winPercentStat = Stat(
            headline = "${(completedGames.count { it.answer().playerGuessedCorrectly() ?: false } / completedGames.count().toFloat() * 100).toInt()}",
            description = "Win %"
        )
        val currentStreakStat = Stat(
            headline = "${winGroups.lastOrNull()?.count() ?: 0}",
            description = "Current Streak"
        )
        val maxStreakStat = Stat(
            headline = "${winGroups.maxByOrNull { group -> group.count() }?.count() ?: 0}",
            description = "Max Streak"
        )

        val guessDistributionRows: List<GuessDistribution.Row> = getWinsByRound(completedGames).mapIndexed()
        { index, int ->
            GuessDistribution.Row(round = index + 1, correctGuessesCount = int)
        }

        return StatsModal(
            stats = listOf(totalGamesPlayedStat, winPercentStat, currentStreakStat, maxStreakStat),
            guessDistribution = GuessDistribution(
                title = "Guess Distribution",
                rows = guessDistributionRows
            )
        )
    }

    fun getWinGroups(gamesList: List<CompletedGame>): List<List<CompletedGame>>
    {
        val winGroups = mutableListOf<MutableList<CompletedGame>>()

        gamesList.forEach()
        { game ->
            if (game.answer().playerGuessedCorrectly() == true)
            {
                winGroups.lastOrNull()?.add(game) ?: winGroups.add(mutableListOf(game))
            }
            else
            {
                winGroups.add(mutableListOf())
            }
        }

        return winGroups.filter { group -> group.isNotEmpty() }
    }

    fun getWinsByRound(gamesList: List<CompletedGame>): List<Int>
    {
        val roundWinTotals = mutableListOf<Int>()

        gamesList.forEach()
        { game ->
            val guessCount = if (game.answer().playerGuessedCorrectly() == true) game.playerGuesses().count() else 0

            if (guessCount > 0)
            {
                for (i in (0 until guessCount))
                {
                    if (roundWinTotals.getOrNull(index = i) == null) roundWinTotals.add(0)
                }

                roundWinTotals[guessCount - 1] = roundWinTotals.get(index = guessCount - 1) + 1
            }
        }

        return roundWinTotals
    }
}