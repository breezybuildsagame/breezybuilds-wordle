package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.Announcement
import org.koin.core.component.inject

fun GameUseCase.getAnnouncement(): Announcement
{
    val announcement: Announcement by inject()

    return announcement
}