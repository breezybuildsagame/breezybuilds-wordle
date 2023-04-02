package com.megabreezy.breezybuilds_wordle.core.ui.app_modal

class AppModal
{
    private var content: AppModalContentRepresentable? = null

    fun content(): AppModalContentRepresentable? = this.content

    fun setContent(newContent: AppModalContentRepresentable?) { this.content = newContent }
}