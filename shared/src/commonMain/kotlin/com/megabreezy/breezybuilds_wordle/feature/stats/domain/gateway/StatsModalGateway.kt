package com.megabreezy.breezybuilds_wordle.feature.stats.domain.gateway

import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.StatsModal

interface StatsModalGateway
{
    fun get(): StatsModal
}