package com.megabreezy.breezybuilds_wordle.feature.game.domain.use_case

import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.Announcement
import com.megabreezy.breezybuilds_wordle.feature.game.domain.model.AnnouncementRepresentable
import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetAnnouncementTests: KoinComponent
{
    @BeforeTest
    fun setUp() { startKoin { modules(GameKoinModule().module()) } }

    @AfterTest
    fun tearDown() = stopKoin()

    @Test
    fun `When use case is invoked - expected Announcement is returned`()
    {
        // given
        val expectedAnnouncement: AnnouncementRepresentable by inject()

        // when
        val sut = GameUseCase().getAnnouncement()
        expectedAnnouncement.setMessage(newMessage = "My new announcement!")

        // then
        assertEquals(expectedAnnouncement.message(), sut.message())
    }
}