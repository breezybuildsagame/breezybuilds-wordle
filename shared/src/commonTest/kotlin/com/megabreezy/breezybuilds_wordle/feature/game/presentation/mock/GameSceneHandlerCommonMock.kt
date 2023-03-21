package com.megabreezy.breezybuilds_wordle.feature.game.presentation.mock

import com.megabreezy.breezybuilds_wordle.feature.game.presentation.GameSceneHandleable

class GameSceneHandlerCommonMock: GameSceneHandleable
{
    var onGameOverDidInvoke = false
    var onGameStartedDidInvoke = false
    var onGuessingWordDidInvoke = false
    var onRevealNextTileDidInvoke = false
    var onRoundCompletedDidInvoke = false
    var onStartingGameDidInvoke = false
    var onAnnouncementShouldShowDidInvoke = false
    var onAnnouncementShouldHideDidInvoke = false

    override fun onAnnouncementShouldShow() { onAnnouncementShouldShowDidInvoke = true }
    override fun onAnnouncementShouldHide() { onAnnouncementShouldHideDidInvoke = true }
    override fun onGameOver() { onGameOverDidInvoke = true }
    override fun onGameStarted() { onGameStartedDidInvoke = true }
    override fun onGuessingWord() { onGuessingWordDidInvoke = true }
    override fun onRevealNextTile() { onRevealNextTileDidInvoke = true }
    override fun onRoundCompleted() { onRoundCompletedDidInvoke = true }
    override fun onStartingGame() { onStartingGameDidInvoke = true }
}