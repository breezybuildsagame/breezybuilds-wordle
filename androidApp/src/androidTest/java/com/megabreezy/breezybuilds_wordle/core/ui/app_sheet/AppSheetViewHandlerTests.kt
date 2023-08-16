package com.megabreezy.breezybuilds_wordle.core.ui.app_sheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.megabreezy.breezybuilds_wordle.android.core.ui.app_sheet.AppSheetViewHandler
import com.megabreezy.breezybuilds_wordle.android.core.ui.app_sheet.rememberAppSheetViewHandler
import com.megabreezy.breezybuilds_wordle.android.help.presentation.HelpSheetComposable
import com.megabreezy.breezybuilds_wordle.core.ui.SceneMock
import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.mock.AppSheetCommonMock
import com.megabreezy.breezybuilds_wordle.feature.help.domain.model.HelpSheet
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppSheetViewHandlerTests
{
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_when_setUp_invoked__AppSheet_setHandler_method_is_invoked_passing_in_self()
    {
        // given
        val appSheetMock = AppSheetCommonMock()
        val sut = AppSheetViewHandler(appSheet = appSheetMock)

        // when
        sut.setUp()

        // then
        Assert.assertEquals(sut, appSheetMock.handler())
    }

    @Test
    fun test_when_onSheetShouldShow_invoked__expected_view_is_displayed()
    {
        // given
        lateinit var sut: AppSheetViewHandler
        val appSheetMock = AppSheetCommonMock()
        val appSheetMockContent = appSheetMock.contentToReturn as HelpSheet

        // when
        composeTestRule.setContent()
        {
            sut = rememberAppSheetViewHandler(appSheet = appSheetMock)

            SceneMock.display()
            {
                MockView(handler = sut)
            }
        }
        sut.onSheetShouldShow(animationDuration = 0L)

        // then
        composeTestRule.onNodeWithContentDescription("${HelpSheetComposable.TagName.CONTENT}")
    }

    @Composable
    fun MockView(
        handler: AppSheetViewHandler
    )
    {
        LaunchedEffect(Unit) { handler.setUp() }

        if (handler.appSheetIsShowing) handler.SheetContent()
    }
}