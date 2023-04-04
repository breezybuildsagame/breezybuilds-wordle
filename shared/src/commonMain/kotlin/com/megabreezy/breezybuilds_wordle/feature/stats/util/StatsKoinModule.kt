package com.megabreezy.breezybuilds_wordle.feature.stats.util

import com.megabreezy.breezybuilds_wordle.feature.stats.data.gateway.StatsModalRepository
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.gateway.StatsModalGateway
import org.koin.core.module.Module

class StatsKoinModule
{
    fun module(): Module = org.koin.dsl.module()
    {
        single<StatsModalGateway> { StatsModalRepository() }
    }
}