package com.megabreezy.breezybuilds_wordle.feature.game.presentation

import com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case.GameUseCase
import com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case.setUpGameEvents

class GameSceneViewModel
{
    fun setUp()
    {
        GameUseCase().setUpGameEvents()
    }
}