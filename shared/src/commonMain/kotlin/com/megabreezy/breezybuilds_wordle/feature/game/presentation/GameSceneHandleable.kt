package com.megabreezy.breezybuilds_wordle.feature.game.presentation

interface GameSceneHandleable
{
    fun onGameOver()
    fun onGameStarted()
    fun onGuessingWord()
    fun onRevealNextTile()
    fun onRoundCompleted()
    fun onStartingGame()
}