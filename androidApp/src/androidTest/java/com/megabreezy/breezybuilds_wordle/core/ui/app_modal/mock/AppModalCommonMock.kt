package com.megabreezy.breezybuilds_wordle.core.ui.app_modal.mock

import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalContentRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalRepresentable
import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalViewHandleable

class AppModalCommonMock: AppModalRepresentable
{
    var contentToReturn: AppModalContentRepresentable? = null
    var handlerToReturn: AppModalViewHandleable? = null
    var setContentPassedInNewContent: AppModalContentRepresentable? = null
    var setHandlerPassedInNewHandler: AppModalViewHandleable? = null

    override fun content(): AppModalContentRepresentable? = this.contentToReturn

    override fun handler(): AppModalViewHandleable? = this.handlerToReturn

    override fun setContent(newContent: AppModalContentRepresentable?)
    {
        this.setContentPassedInNewContent = newContent
    }

    override fun setHandler(newHandler: AppModalViewHandleable?)
    {
        this.setHandlerPassedInNewHandler = newHandler
    }
}