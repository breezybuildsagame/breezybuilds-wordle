package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard
import com.megabreezy.breezybuilds_wordle.feature.game.presentation.GameSceneHandleable
import kotlinx.coroutines.delay

fun GameUseCase.setUpGameEvents(
    sceneHandler: GameSceneHandleable? = null,
    announcementDelay: Long = 1000L
)
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

                        getAnnouncement().setMessage(newMessage = "Correct! Thanks for playing!")
                        sceneHandler?.onGameOver()
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
                            if (tile.letter() == getGameAnswer().word()[index])
                                tile.setState(newState = GameBoard.Tile.State.CORRECT)
                            else if (getGameAnswer().word().contains("${tile.letter()}"))
                                tile.setState(newState = GameBoard.Tile.State.CLOSE)
                            else tile.setState(newState = GameBoard.Tile.State.INCORRECT)

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

                        try
                        {
                            getGameBoard().setNewActiveRow()
                            sceneHandler?.onRoundCompleted()
                        }
                        catch (e: GameBoard.SetNewActiveRowFailedException)
                        {
                            getAnnouncement().setMessage(newMessage = "Game Over")
                            sceneHandler?.onGameOver()
                        }
                    }
                    catch (e: GameUseCase.GuessWordFailedNotInWordsListException)
                    {
                        println(e)
                        getAnnouncement().setMessage(newMessage = e.message)
                        sceneHandler?.let()
                        { handler ->
                            handler.onAnnouncementShouldShow()
                            delay(timeMillis = announcementDelay)
                            getAnnouncement().setMessage(newMessage = null)
                            handler.onAnnouncementShouldHide()
                        }
                    }
                } else Unit
            }
        }
    }
}