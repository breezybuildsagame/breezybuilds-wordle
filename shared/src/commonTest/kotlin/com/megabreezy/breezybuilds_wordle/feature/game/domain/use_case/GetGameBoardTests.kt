package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameBoard
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetGameBoardTests: KoinComponent
{
    @BeforeTest
    fun setUp()
    {
        startKoin()
        {
            modules(
                CoreKoinModule().mockModule(),
                GameKoinModule().module()
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `When use case is invoked - expected GameBoard is returned`()
    {
        // given
        val expectedGameBoard: GameBoard by inject()
        val expectedRows = listOf(
            listOf(GameBoard.Tile(letter = 'A'), GameBoard.Tile(letter = 'Z'))
        )

        // when
        val actualGameBoard = GameUseCase().getGameBoard()
        expectedGameBoard.setRows(newRows = expectedRows)

        // then
        assertEquals(expectedGameBoard.rows(), actualGameBoard.rows())
    }
}