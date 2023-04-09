package com.megabreezy.breezybuilds_wordle.core.ui.app_modal

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AppModal: AppModalRepresentable
{
    private var content: AppModalContentRepresentable? = null

    private var viewHandler: AppModalViewHandleable? = null

    override fun content(): AppModalContentRepresentable? = this.content

    override fun handler(): AppModalViewHandleable? = this.viewHandler

    override fun setContent(newContent: AppModalContentRepresentable?) { this.content = newContent }

    override fun setHandler(newHandler: AppModalViewHandleable?) { this.viewHandler = newHandler }
}

class AppModalHelper(): KoinComponent
{
    private val appModal: AppModalRepresentable by inject()

    fun appModal() = this.appModal
}