package com.megabreezy.breezybuilds_wordle.core.ui.app_modal.mock

import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalContentRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalViewHandleable

class AppModalCommonMock: AppModalRepresentable
{
    var contentToReturn: AppModalContentRepresentable? = null

    override fun content(): AppModalContentRepresentable? = this.contentToReturn

    override fun handler(): AppModalViewHandleable?
    {
        TODO("Not yet implemented")
    }

    override fun setContent(newContent: AppModalContentRepresentable?) { this.contentToReturn = newContent }

    override fun setHandler(newHandler: AppModalViewHandleable?)
    {
        TODO("Not yet implemented")
    }
}