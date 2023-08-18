package com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.mock

import com.megabreezy.breezybuilds_wordle.core.ui.app_sheet.AppSheetContentRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.button.ButtonRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.button.mock.ButtonCommonMock

class AppSheetContentCommonMock: AppSheetContentRepresentable
{
    var buttonClickDidInvoke = false
    var closeButtonToReturn = ButtonCommonMock { buttonClickDidInvoke = true }

    override fun closeButton(): ButtonRepresentable = closeButtonToReturn
}