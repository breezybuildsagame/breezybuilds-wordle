package com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.mock

import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheetContentRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheetRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheetViewHandleable

class AppSheetCommonMock: AppSheetRepresentable
{
    var setContentPassedInNewContent: AppSheetContentRepresentable? = null
    var setHandlerPassedInNewHandler: AppSheetViewHandleable? = null

    override fun content(): AppSheetContentRepresentable? = this.setContentPassedInNewContent

    override fun handler(): AppSheetViewHandleable? = this.setHandlerPassedInNewHandler

    override fun setContent(newContent: AppSheetContentRepresentable?) { this.setContentPassedInNewContent = newContent }

    override fun setHandler(newHandler: AppSheetViewHandleable?) { this.setHandlerPassedInNewHandler = newHandler }
}