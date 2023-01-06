package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard
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
                val tileToSet = getGameBoard().activeRow()?.firstOrNull()
                {
                    it.letter() == null && key.letter() != null && key.letters() != "ENTER"
                }
                val tileToBackspace = getGameBoard().activeRow()?.lastOrNull()
                {
                    it.letter() != null && key.letters() == "BACKSPACE"
                }

                tileToBackspace?.let()
                { tile ->
                    tile.setLetter(newLetter = null)
                    sceneHandler?.onRevealNextTile()
                }
                ?: tileToSet?.let()
                { tile ->
                    tile.setLetter(newLetter = key.letter())
                    sceneHandler?.onRevealNextTile()
                }
                ?: if (key.letters() == "ENTER")
                {
                    try
                    {
                        guessWord()
                    }
                    catch(e: GameUseCase.GuessWordInvalidGuessException)
                    {
                        println(e)
                    }
                    catch(e: GameUseCase.GuessWordFailedMismatchException)
                    {
                        println(e)
                        getGameBoard().activeRow()?.forEachIndexed()
                        { index, tile ->
                            for (keyRow in getGameKeyboard().rows())
                            {
                                for (currentKey in keyRow)
                                {
                                    if (currentKey.backgroundColor() == GameKeyboard.Key.BackgroundColor.CORRECT) continue

                                    if (
                                        currentKey.letter() == tile.letter()
                                        && !getGameAnswer().word().contains(currentKey.letter()!!)
                                    )
                                    {
                                        currentKey.setBackgroundColor(newBackgroundColor = GameKeyboard.Key.BackgroundColor.NOT_FOUND)
                                    }
                                    else if
                                    (
                                        currentKey.letter() == tile.letter()
                                        && getGameAnswer().word().contains(currentKey.letter()!!)
                                        && currentKey.letter() !=  getGameAnswer().word()[index]
                                    )
                                    {
                                        currentKey.setBackgroundColor(newBackgroundColor = GameKeyboard.Key.BackgroundColor.NEARBY)
                                    }

                                    if (currentKey.letter() == tile.letter() && tile.letter() == getGameAnswer().word()[index])
                                    {
                                        currentKey.setBackgroundColor(newBackgroundColor = GameKeyboard.Key.BackgroundColor.CORRECT)
                                    }
                                }
                            }
                        }
                    }
                } else Unit
            }
        }
    }
}