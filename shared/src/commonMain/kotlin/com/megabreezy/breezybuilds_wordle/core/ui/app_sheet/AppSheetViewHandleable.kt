package com.megabreezy.breezybuilds_wordle.core.ui.app_sheet

interface AppSheetViewHandleable
{
    fun onSheetShouldShow(animationDuration: Long)
    fun onSheetShouldHide(animationDuration: Long)
}