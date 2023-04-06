package com.megabreezy.breezybuilds_wordle.core.ui.app_modal

class AppModal: AppModalRepresentable
{
    private var content: AppModalContentRepresentable? = null

    private var viewHandler: AppModalViewHandleable? = null

    override fun content(): AppModalContentRepresentable? = this.content

    override fun handler(): AppModalViewHandleable? = this.viewHandler

    override fun setContent(newContent: AppModalContentRepresentable?) { this.content = newContent }

    override fun setHandler(newHandler: AppModalViewHandleable?) { this.viewHandler = newHandler }
}