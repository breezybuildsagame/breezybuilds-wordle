package com.megabreezy.breezybuilds_wordle.feature.game.presentation

interface GameSceneHandleable
{
    fun onAnnouncementShouldShow()
    fun onAnnouncementShouldHide()
    fun onGameOver()
    fun onGameStarted()
    fun onGuessingWord()
    fun onRevealNextTile()
    fun onRoundCompleted()
    fun onStartingGame()
}