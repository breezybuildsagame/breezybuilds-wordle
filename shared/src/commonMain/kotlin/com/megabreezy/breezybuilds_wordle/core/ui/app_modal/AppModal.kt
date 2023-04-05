package com.megabreezy.breezybuilds_wordle.core.ui.app_modal

class AppModal: AppModalRepresentable
{
    private var content: AppModalContentRepresentable? = null

    override fun content(): AppModalContentRepresentable? = this.content

    override fun handler(): AppModalViewHandleable?
    {
        TODO("Not yet implemented")
    }

    override fun setContent(newContent: AppModalContentRepresentable?) { this.content = newContent }

    override fun setHandler(newHandler: AppModalViewHandleable?)
    {
        TODO("Not yet implemented")
    }
}