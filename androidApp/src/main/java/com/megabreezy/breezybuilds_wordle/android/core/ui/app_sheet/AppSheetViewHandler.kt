package com.megabreezy.breezybuilds_wordle.android.core.ui.app_sheet

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.megabreezy.breezybuilds_wordle.android.help.presentation.HelpSheetComposable
import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheetHelper
import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheetRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheetViewHandleable

class AppSheetViewHandler(
    private val appSheet: AppSheetRepresentable = AppSheetHelper().appSheet()
): AppSheetViewHandleable
{
    var appSheetIsShowing by mutableStateOf(false)

    fun setUp() { appSheet.setHandler(newHandler = this) }

    override fun onSheetShouldShow(animationDuration: Long)
    {
        appSheetIsShowing = true
    }

    override fun onSheetShouldHide(animationDuration: Long)
    {
        TODO("Not yet implemented")
    }

    @Composable
    fun SheetContent()
    {
        HelpSheetComposable.Component()
    }
}

@Composable
fun rememberAppSheetViewHandler(
    appSheet: AppSheetRepresentable = AppSheetHelper().appSheet()
) = remember {
    AppSheetViewHandler(appSheet = appSheet)
}