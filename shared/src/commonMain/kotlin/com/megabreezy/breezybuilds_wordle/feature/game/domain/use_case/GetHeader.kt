package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.GameNavigationHandleable
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameHeader
import org.koin.core.component.inject

fun GameUseCase.getHeader(): GameHeader
{
    val navigator: GameNavigationHandleable by inject()

    return GameHeader(
        title = "WORDLE",
        options = listOf(
            GameHeader.Option(
                iconResourceId = "game_image_help_icon",
                onClick = { navigator.onHelpOptionClicked() }
            ),
            GameHeader.Option(
                iconResourceId = "game_image_stats_icon",
                onClick = { navigator.onStatsOptionClicked() }
            ),
            GameHeader.Option(
                iconResourceId = "game_image_settings_icon",
                onClick = { navigator.onSettingsOptionClicked() }
            )
        )
    )
}