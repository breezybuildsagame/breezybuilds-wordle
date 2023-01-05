package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.presentation.GameSceneHandleable

fun GameUseCase.setUpGameEvents(sceneHandler: GameSceneHandleable? = null)
{
    getGameBoard().reset()
    getGameKeyboard().reset()
    getAnnouncement().setMessage(newMessage = null)

    for (row in getGameKeyboard().rows())
    {
        for (key in row)
        {
            key.setOnClick()
            {
                val tileToSet = getGameBoard().activeRow()?.firstOrNull { it.letter() == null }

                tileToSet?.let()
                { tile ->
                    tile.setLetter(newLetter = key.letter())
                    sceneHandler?.onRevealNextTile()
                }
            }
        }
    }
}