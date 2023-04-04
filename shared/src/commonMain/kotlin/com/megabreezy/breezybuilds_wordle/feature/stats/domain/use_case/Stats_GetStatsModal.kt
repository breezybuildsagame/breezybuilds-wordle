package com.megabreezy.breezybuilds_wordle.feature.stats.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.stats.domain.gateway.StatsModalGateway
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.StatsModal
import org.koin.core.component.inject

fun StatsUseCase.getStatsModal(): StatsModal
{
    val repository: StatsModalGateway by inject()

    return repository.get()
}