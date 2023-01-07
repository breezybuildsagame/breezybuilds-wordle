package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.feature.game.domain.GameNavigationHandleable
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.GameHeader
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class GetHeaderTests
{
    lateinit var navigator: MockNavigator

    @BeforeTest
    fun setUp()
    {
        navigator = MockNavigator()

        startKoin()
        {
            modules(
                CoreKoinModule().mockModule(),
                GameKoinModule().module(),
                module { single<GameNavigationHandleable> { navigator } }
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `when use case invoked - header title method returns expected string value`()
    {
        // given
        val expectedTitle = "WORDLE"

        // when
        val sut = GameUseCase().getHeader()

        // then
        assertEquals(expectedTitle, sut.title())
    }

    @Test
    fun `when use case invoked - options method returns expected list of options`()
    {
        // given
        val expectedOptions = listOf(
            GameHeader.Option(
                iconResourceId = "game_image_help_icon"
            ),
            GameHeader.Option(
                iconResourceId = "game_image_stats_icon"
            ),
            GameHeader.Option(
                iconResourceId = "game_image_settings_icon"
            )
        )

        // when
        val sut = GameUseCase().getHeader()

        // then
        assertEquals(expectedOptions, sut.options())
    }

    @Test
    fun `when use case invoked and help option is clicked - navigation handleable onHelpOptionClicked method is invoked`()
    {
        // given
        val sut = GameUseCase().getHeader()

        // when
        sut.options().first { it.iconResourceId() == "game_image_help_icon" }.click()

        // then
        assertTrue(navigator.onHelpOptionClickedDidInvoke)
    }

    @Test
    fun `when use case invoked and stats option is clicked - navigation handleable onStatsOptionClicked method is invoked`()
    {
        // given
        val sut = GameUseCase().getHeader()

        // when
        sut.options().first { it.iconResourceId() == "game_image_stats_icon" }.click()

        // then
        assertTrue(navigator.onStatsOptionClickedDidInvoke)
    }

    @Test
    fun `when use case invoked and settings option is clicked - navigation handleable onSettingsOptionClicked method is invoked`()
    {
        // given
        val sut = GameUseCase().getHeader()

        // when
        sut.options().first { it.iconResourceId() == "game_image_settings_icon" }.click()

        // then
        assertTrue(navigator.onSettingsOptionClickedDidInvoke)
    }

    class MockNavigator: GameNavigationHandleable
    {
        var onHelpOptionClickedDidInvoke = false
        var onStatsOptionClickedDidInvoke = false
        var onSettingsOptionClickedDidInvoke = false

        override fun onHelpOptionClicked() { onHelpOptionClickedDidInvoke = true }
        override fun onStatsOptionClicked() { onStatsOptionClickedDidInvoke = true }
        override fun onSettingsOptionClicked() { onSettingsOptionClickedDidInvoke = true }
    }
}