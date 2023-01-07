package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameKeyboard
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetGameKeyboardTests: KoinComponent
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
    fun `When use case is invoked - expected GameKeyboard is returned`()
    {
        // given
        val expectedGameKeyboard: GameKeyboard by inject()
        val expectedKey = expectedGameKeyboard.rows().first().first()

        // when
        val actualKeyboard = GameUseCase().getGameKeyboard()
        val actualKey = actualKeyboard.rows().first().first()
        expectedKey.setIsEnabled(newIsEnabled = !expectedKey.isEnabled())

        // then
        assertEquals(expectedKey.isEnabled(), actualKey.isEnabled())
    }
}