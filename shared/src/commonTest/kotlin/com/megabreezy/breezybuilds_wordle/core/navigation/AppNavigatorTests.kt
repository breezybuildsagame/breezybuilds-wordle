package com.megabreezy.breezybuilds_wordle.core.navigation

import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.mock.AppModalCommonMock
import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.mock.AppModalViewHandlerCommonMock
import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.core.util.Scenario
import com.megabreezy.breezybuilds_wordle.feature.stats.data.gateway.mock.StatsModalRepositoryCommonMock
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.gateway.StatsModalGateway
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.GuessDistribution
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.Stat
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.StatsModal
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.use_case.StatsUseCase
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.use_case.getStatsModal
import com.megabreezy.breezybuilds_wordle.feature.stats.util.StatsKoinModule
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
class AppNavigatorTests
{
    private lateinit var appModal: AppModalCommonMock
    private lateinit var statsModalRepository: StatsModalRepositoryCommonMock

    @BeforeTest
    fun setUp()
    {
        appModal = AppModalCommonMock()
        statsModalRepository = StatsModalRepositoryCommonMock()

        startKoin()
        {
            modules(
                CoreKoinModule().mockModule(),
                StatsKoinModule().module(),
                module { single<AppModalRepresentable> { appModal } },
                module { single<StatsModalGateway> { statsModalRepository } }
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `when navigate invoked with AppRoute and NavigationDirection - sceneNavigator navigate method is invoked with expected parameters`()
    {
        // given
        val sut = AppNavigator()
        val sceneNavigator = MockSceneNavigator()
        sut.setSceneNavigator(newSceneNavigator = sceneNavigator)

        // when
        sut.navigate(route = AppRoute.GAME, NavigationDirection.BACKWARD)

        // then
        assertEquals(AppRoute.GAME, sceneNavigator.navigatedRoute)
        assertEquals(NavigationDirection.BACKWARD, sceneNavigator.navigatedDirection)
    }

    @Test
    fun `when navigate invoked with AppRoute - currentRout method returns expected AppRoute`()
    {
        // given
        val sut = AppNavigator()
        sut.navigate(route = AppRoute.SETTINGS)

        // when
        sut.navigate(route = AppRoute.GAME)

        // then
        assertEquals(AppRoute.GAME, sut.currentRoute())
    }

    @Test
    fun `When navigate invoked with Stats AppRoute - AppRoute does not change`()
    {
        // given
        val sut = AppNavigator()
        sut.navigate(route = AppRoute.GAME)

        // when
        sut.navigate(route = AppRoute.STATS_GAME_OVER)

        // then
        assertEquals(AppRoute.GAME, sut.currentRoute())
    }

    @Test
    fun `When navigate invoked with Help AppRoute - AppRoute does not change`()
    {
        // given
        val sut = AppNavigator()
        sut.navigate(route = AppRoute.GAME)

        // when
        sut.navigate(route = AppRoute.HELP)

        // then
        assertEquals(AppRoute.GAME, sut.currentRoute())
    }

    @Test
    fun `When navigate invoked with Help AppRoute - the injected AppSheetRepresentable setContent method is invoked passing in GetHelpSheet use case`()
    {
        // given

        // when

        // then
        assertTrue(false)
    }

    @Test
    fun `When navigate invoked with Stats AppRoute - the injected AppSheetRepresentable onSheetShouldShow is invoked with expected animation duration`()
    {
        // given

        // when

        // then
        assertTrue(false)
    }

    @Test
    fun `When navigate invoked with Stats AppRoute - the injected AppModalRepresentable setContent method is invoked passing in GetStatsModal use case`()
    {
        // given
        statsModalRepository.getStatsModalToReturn = StatsModal(
            stats = listOf(Stat(headline = "10", description = "SOME STAT")),
            guessDistribution = GuessDistribution(
                title = "Guess Distribution",
                rows = listOf(GuessDistribution.Row(round = 1, correctGuessesCount = 11))
            )
        )
        val expectedAppModalContent = StatsUseCase().getStatsModal()
        val sut = AppNavigator()

        // when
        sut.navigate(route = AppRoute.STATS_GAME_OVER)

        // then
        assertEquals(expectedAppModalContent, appModal.contentToReturn)
    }

    @Test
    fun `When navigate invoked with Stats AppRoute - the AppModalViewHandler onModalShouldShow is invoked with expected animation duration`()
    {
        // given
        val mockModalViewHandler = AppModalViewHandlerCommonMock()
        appModal.setHandler(newHandler = mockModalViewHandler)
        val sut = AppNavigator()

        // when
        sut.navigate(route = AppRoute.STATS_GAME_OVER)

        // then
        assertEquals(300L, mockModalViewHandler.onModalShouldShowPassedInAnimationDuration)
    }

    @Test
    fun `When a game is in progress and the StatsModal is presented - the playAgainButton method returns null`()
    {
        // given
        stopKoin()
        startKoin()
        {
            modules(
                CoreKoinModule(listOf(Scenario.GUESSES_FOUND)).mockModule(),
                StatsKoinModule().module(),
                module { single<AppModalRepresentable> { appModal } },
                module { single<StatsModalGateway> { statsModalRepository } }
            )
        }
        val mockModalViewHandler = AppModalViewHandlerCommonMock()
        appModal.setHandler(newHandler = mockModalViewHandler)
        val sut = AppNavigator()

        // when
        sut.navigate(route = AppRoute.STATS_GAME_OVER)

        // then
        assertNull((appModal.content() as StatsModal).playAgainButton())
    }

    @Test
    fun `When the StatsModal playAgain button is clicked - the AppModalViewHandler onModalShouldHide method is invoked`()
    {
        // given
        val mockModalViewHandler = AppModalViewHandlerCommonMock()
        appModal.setHandler(newHandler = mockModalViewHandler)
        val sut = AppNavigator()
        sut.navigate(route = AppRoute.STATS_GAME_OVER)

        if (appModal.content() is StatsModal)
        {
            // when
            val playAgainButton = (appModal.content() as StatsModal).playAgainButton()
            runBlocking { playAgainButton?.click() }

            // then
            assertEquals("Play Again", playAgainButton?.label())
            assertEquals(300L, mockModalViewHandler.onModalShouldHidePassedInAnimationDuration)
        }
        else
        {
            fail(message = "Expected modal content not found.")
        }
    }

    @Test
    fun `When the StatsModal playAgain button is clicked - the current route is set to GAME and the sceneNavigator's navigate method is invoked`()
    {
        // given
        val mockSceneNavigator = MockSceneNavigator()
        val mockModalViewHandler = AppModalViewHandlerCommonMock()
        appModal.setHandler(newHandler = mockModalViewHandler)
        val sut = AppNavigator(modalAnimationDuration = 100L)
        sut.navigate(route = AppRoute.GAME)
        sut.navigate(route = AppRoute.STATS_GAME_OVER)
        sut.setSceneNavigator(newSceneNavigator = mockSceneNavigator)
        mockSceneNavigator.navigatedRoute = null
        mockSceneNavigator.navigatedDirection = null

        if (appModal.content() is StatsModal)
        {
            // when
            val playAgainButton = (appModal.content() as StatsModal).playAgainButton()
            val processingTime = measureTime()
            {
                runBlocking { playAgainButton?.click() }
            }

            // then
            assertTrue(processingTime.inWholeMilliseconds > 90L)
            assertEquals(AppRoute.GAME, sut.currentRoute())
            assertEquals(AppRoute.GAME, mockSceneNavigator.navigatedRoute)
            assertEquals(NavigationDirection.INSTANT, mockSceneNavigator.navigatedDirection)
            assertEquals(1, sut.routes().count())
        }
        else
        {
            fail(message = "Expected modal content not found.")
        }
    }

    class MockSceneNavigator: SceneNavigationHandleable
    {
        var navigatedRoute: AppRoute? = null
        var navigatedDirection: NavigationDirection? = null

        override fun navigate(route: AppRoute, direction: NavigationDirection)
        {
            navigatedRoute = route
            navigatedDirection = direction
        }
    }
}