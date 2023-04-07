package com.megabreezy.breezybuilds_wordle.core.ui.app_modal

interface AppModalRepresentable
{
    fun content(): AppModalContentRepresentable?

    fun handler(): AppModalViewHandleable?

    fun setContent(newContent: AppModalContentRepresentable? = null)

    fun setHandler(newHandler: AppModalViewHandleable? = null)
}