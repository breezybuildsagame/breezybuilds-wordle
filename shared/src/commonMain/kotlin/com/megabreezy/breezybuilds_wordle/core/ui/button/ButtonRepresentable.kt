package com.megabreezy.breezybuilds_wordle.core.ui.button

interface ButtonRepresentable
{
    suspend fun click()
    fun imageResourceId(): String? = null
    fun label(): String? = null
}