package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.AnnouncementRepresentable
import org.koin.core.component.inject

fun GameUseCase.getAnnouncement(): AnnouncementRepresentable
{
    val announcement: AnnouncementRepresentable by inject()

    return announcement
}