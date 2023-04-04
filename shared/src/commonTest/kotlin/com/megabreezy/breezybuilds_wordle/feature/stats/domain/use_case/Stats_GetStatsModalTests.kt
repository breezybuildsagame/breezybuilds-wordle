package com.megabreezy.breezybuilds_wordle.feature.stats.domain.use_case

import com.megabreezy.breezybuilds_wordle.core.util.CoreKoinModule
import com.megabreezy.breezybuilds_wordle.feature.stats.data.gateway.mock.StatsModalRepositoryCommonMock
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.gateway.StatsModalGateway
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.Stat
import com.megabreezy.breezybuilds_wordle.feature.stats.domain.model.StatsModal
import com.megabreezy.breezybuilds_wordle.feature.stats.util.StatsKoinModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.*

class StatsGetStatsModalTests
{
    lateinit var repository: StatsModalRepositoryCommonMock

    @BeforeTest
    fun setUp()
    {
        repository = StatsModalRepositoryCommonMock()

        startKoin()
        {
            modules(
                CoreKoinModule().mockModule(),
                StatsKoinModule().module(),
                module { single<StatsModalGateway> { repository } }
            )
        }
    }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `When use case invoked - injected repository get method is invoked`()
    {
        // given, when
        StatsUseCase().getStatsModal()

        // then
        assertNotNull(repository.getStatsModalToReturn)
    }

    @Test
    fun `When use case invoked and repository returns a StatsModal - expected StatsModal is returned`()
    {
        // given
        val expectedStatsModal = StatsModal(
            stats = listOf(Stat(headline = "something", description = "interesting"))
        )
        repository.getStatsModalToReturn = expectedStatsModal

        // when
        val actualStatsModal = StatsUseCase().getStatsModal()

        // then
        assertEquals(expectedStatsModal, actualStatsModal)
    }
}