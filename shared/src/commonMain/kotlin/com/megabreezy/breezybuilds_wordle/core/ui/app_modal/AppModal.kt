package com.megabreezy.breezybuilds_wordle.core.ui.app_modal

class AppModal: AppModalRepresentable
{
    private var content: AppModalContentRepresentable? = null

    override fun content(): AppModalContentRepresentable? = this.content

    fun setContent(newContent: AppModalContentRepresentable?) { this.content = newContent }
}