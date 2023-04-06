package com.megabreezy.breezybuilds_wordle.core.ui.app_modal

import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.mock.AppModalViewHandlerCommonMock
import com.megabreezy.breezybuilds_wordle.core.ui.button.ButtonRepresentable
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppModalTests
{
    @Test
    fun `When setContent invoked with new content - content method returns expected value`()
    {
        // given
        val mockContent = MockAppModalContent()
        val sut = AppModal()

        // when
        sut.setContent(newContent = mockContent)
        runBlocking { sut.content()?.closeButton()?.click() }

        // then
        assertTrue(mockContent.closeButton.clickDidInvoke)
    }

    @Test
    fun `When setHandler invoked with newHandler - handler method returns expected value`()
    {
        // given
        val mockHandler = AppModalViewHandlerCommonMock()
        val sut = AppModal()

        // when
        sut.setHandler(newHandler = mockHandler)
        sut.handler()?.onModalShouldShow(animationDuration = 10L)

        // then
        assertEquals(10L, mockHandler.onModalShouldShowPassedInAnimationDuration)
    }

    class MockAppModalContent: AppModalContentRepresentable
    {
        var closeButton = MockAppModalButtonRepresentable()
        override fun closeButton() = closeButton
    }

    class MockAppModalButtonRepresentable: ButtonRepresentable
    {
        var clickDidInvoke = false

        override suspend fun click() { clickDidInvoke = true }
    }
}