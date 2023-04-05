package com.megabreezy.breezybuilds_wordle.core.ui.app_modal.mock

import com.megabreezy.breezybuilds_wordle.core.ui.app_modal.AppModalViewHandleable

class AppModalViewHandlerCommonMock: AppModalViewHandleable
{
    var onModalShouldShowPassedInAnimationDuration: Long? = null
    var onModalShouldHidePassedInAnimationDuration: Long? = null

    override fun onModalShouldShow(animationDuration: Long) { onModalShouldShowPassedInAnimationDuration = animationDuration }

    override fun onModalShouldHide(animationDuration: Long) { onModalShouldHidePassedInAnimationDuration = animationDuration }
}