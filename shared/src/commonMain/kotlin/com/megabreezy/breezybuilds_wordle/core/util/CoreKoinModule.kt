package com.megabreezy.breezybuilds_wordle.core.util

import com.megabreezy.breezybuilds_wordle.core.navigation.AppNavigationHandleable
import com.megabreezy.breezybuilds_wordle.core.navigation.AppNavigator
import org.koin.core.module.Module
import org.koin.dsl.module

class CoreKoinModule
{
    fun module(): Module = module()
    {
        single<AppNavigationHandleable> { AppNavigator() }
    }
}