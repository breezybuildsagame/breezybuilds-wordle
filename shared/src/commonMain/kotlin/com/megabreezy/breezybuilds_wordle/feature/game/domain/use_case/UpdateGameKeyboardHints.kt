package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard

suspend fun GameUseCase.updateGameKeyboardHints()
{
    getGameBoard().activeRow()?.forEachIndexed()
    { index, tile ->
        for (currentKey in getGameKeyboard().rows().flatten())
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