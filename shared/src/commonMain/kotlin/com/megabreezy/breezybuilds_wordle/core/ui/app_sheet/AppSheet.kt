package com.megabreezy.breezybuilds_wordle.core.ui.app_sheet

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppSheet: AppSheetRepresentable
{
    private var content: AppSheetContentRepresentable? = null
    private var handler: AppSheetViewHandleable? = null

    override fun content(): AppSheetContentRepresentable? = this.content

    override fun handler(): AppSheetViewHandleable? = this.handler

    override fun setContent(newContent: AppSheetContentRepresentable?) { this.content = newContent }

    override fun setHandler(newHandler: AppSheetViewHandleable?) { this.handler = newHandler }
}

class AppSheetHelper: KoinComponent
{
    fun appSheet(): AppSheetRepresentable
    {
        val sharedInstance: AppSheetRepresentable by inject()

        return sharedInstance
    }
}