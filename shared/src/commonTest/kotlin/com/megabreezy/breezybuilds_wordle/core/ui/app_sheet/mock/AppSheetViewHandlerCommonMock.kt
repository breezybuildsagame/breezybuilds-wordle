package com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.mock

import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheetViewHandleable

class AppSheetViewHandlerCommonMock: AppSheetViewHandleable
{
    var onSheetShouldShowPassedInAnimationDuration: Long? = null
    var onSheetShouldHidePassedInAnimationDuration: Long? = null

    override fun onSheetShouldShow(animationDuration: Long)
    {
        onSheetShouldShowPassedInAnimationDuration = animationDuration
    }

    override fun onSheetShouldHide(animationDuration: Long)
    {
        onSheetShouldHidePassedInAnimationDuration = animationDuration
    }
}