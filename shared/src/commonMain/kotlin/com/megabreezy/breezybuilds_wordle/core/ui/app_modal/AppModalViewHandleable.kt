package com.megabreezy.breezybuilds_wordle.core.ui.app_modal

interface AppModalViewHandleable
{
    fun onModalShouldShow(animationDuration: Long)
    fun onModalShouldHide(animationDuration: Long)
}