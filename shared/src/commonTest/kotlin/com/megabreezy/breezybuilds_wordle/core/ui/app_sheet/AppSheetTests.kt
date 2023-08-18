package com.megabreezy.breezybuilds_wordle.core.ui.app_sheet

import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.mock.AppSheetContentCommonMock
import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.mock.AppSheetViewHandlerCommonMock
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppSheetTests
{
    @Test
    fun when_setContent_method_invoked__content_method_returns_expected_AppSheetContentRepresentable()
    {
        // given
        val expectedContent = AppSheetContentCommonMock()
        val sut = AppSheet()

        // when
        sut.setContent(newContent = expectedContent)
        runBlocking { sut.content()?.closeButton()?.click() }

        // then
        assertTrue(expectedContent.buttonClickDidInvoke)
    }

    @Test
    fun when_setHandler_method_invoked__handler_method_returns_expected_AppSheetViewHandleable()
    {
        // given
        val expectedHandler = AppSheetViewHandlerCommonMock()
        val sut = AppSheet()

        // when
        sut.setHandler(newHandler = expectedHandler)
        sut.handler()?.onSheetShouldHide(animationDuration = 100L)
        sut.handler()?.onSheetShouldShow(animationDuration = 243L)

        // then
        assertEquals(243L, expectedHandler.onSheetShouldShowPassedInAnimationDuration)
        assertEquals(100L, expectedHandler.onSheetShouldHidePassedInAnimationDuration)
    }
}