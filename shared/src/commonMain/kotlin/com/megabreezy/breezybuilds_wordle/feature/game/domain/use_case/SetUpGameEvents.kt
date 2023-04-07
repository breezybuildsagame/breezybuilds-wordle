package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.GameNavigationHandleable
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameAnswerGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.GameGuessGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.gateway.SavedGameGateway
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard
import com.megabreezy.breezybuilds_wordle.feature.game.presentation.GameSceneHandleable
import kotlinx.coroutines.delay
import org.koin.core.component.inject

suspend fun GameUseCase.setUpGameEvents(
    sceneHandler: GameSceneHandleable? = null,
    announcementDelay: Long = 1000L
)
{
    val answerRepository: GameAnswerGateway by inject()
    val guessRepository: GameGuessGateway by inject()
    val savedGameRepository: SavedGameGateway by inject()
    val gameNavigationHandler: GameNavigationHandleable by inject()

    getGameBoard(resetIfNecessary = true, reloadIfNecessary = true)
    getGameKeyboard().reset()
    getAnnouncement().setMessage(newMessage = null)

    for (key in getGameKeyboard().rows().flatten())
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

                    answerRepository.updateAnswerGuessed(existingAnswer = getGameAnswer())
                    savedGameRepository.create()
                    guessRepository.clear()
                    getAnnouncement().setMessage(newMessage = "Correct! Thanks for playing!")
                    sceneHandler?.onGameOver()
                    delay(timeMillis = announcementDelay)
                    gameNavigationHandler.onGameOver()
                }
                catch(e: GameUseCase.GuessWordInvalidGuessException)
                {
                    println(e)
                }
                catch(e: GameUseCase.GuessWordFailedMismatchException)
                {
                    println(e)

                    finalizeActiveGameBoardRow()

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

                    try
                    {
                        getGameBoard().setNewActiveRow()
                        sceneHandler?.onRoundCompleted()
                    }
                    catch (e: GameBoard.SetNewActiveRowFailedException)
                    {
                        answerRepository.updateAnswerNotGuessed(existingAnswer = getGameAnswer())
                        savedGameRepository.create()
                        guessRepository.clear()
                        getAnnouncement().setMessage(newMessage = "Game Over")
                        sceneHandler?.onGameOver()
                        delay(timeMillis = announcementDelay)
                        gameNavigationHandler.onGameOver()
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