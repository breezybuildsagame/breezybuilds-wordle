package com.megabreezy.breezybuilds_wordle.feature.game.domain.mock

import com.megabreezy.breezybuilds_wordle.feature.game.domain.GameNavigationHandleable

class GameNavigationHandlerCommonMock: GameNavigationHandleable
{
    var onHelpOptionClickedDidInvoke = false
    var onStatsOptionClickedDidInvoke = false
    var onSettingsOptionClickedDidInvoke = false
    var onGameOverDidInvoke = false

    override fun onHelpOptionClicked() { onHelpOptionClickedDidInvoke = true }

    override fun onStatsOptionClicked() { onStatsOptionClickedDidInvoke = true }

    override fun onSettingsOptionClicked() { onSettingsOptionClickedDidInvoke = true }

    override fun onGameOver() { onGameOverDidInvoke = true }
}