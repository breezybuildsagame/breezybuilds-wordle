package com.megabreezy.breezybuilds_wordle.core.ui.app_sheet

interface AppSheetRepresentable
{
    fun content(): AppSheetContentRepresentable?

    fun handler(): AppSheetViewHandleable?

    fun setContent(newContent: AppSheetContentRepresentable? = null)

    fun setHandler(newHandler: AppSheetViewHandleable? = null)
}