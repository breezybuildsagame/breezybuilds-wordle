package com.megabreezy.breezybuilds_wordle.core.util

import com.megabreezy.breezybuilds_wordle.feature.game.util.GameKoinModule
import org.koin.core.KoinApplication
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import kotlin.native.concurrent.ThreadLocal

// Reference: https://github.com/touchlab/KaMPKit/blob/main/shared/src/commonMain/kotlin/co/touchlab/kampkit/Koin.kt
fun initKoin(
    scenarios: List<Scenario>,
    appDeclaration: KoinAppDeclaration = {}
) = startKoin()
{
    appDeclaration()

    modules(
        CoreKoinModule(scenarios = scenarios).mockModule(),
        GameKoinModule().module()
    )

    KoinPlatformManager.setApp(newApp = this)
}

// called by iOS/Android
@ThreadLocal
object KoinPlatformManager
{
    private var app: KoinApplication? = null

    fun start(scenarios: List<Scenario>)
    {
        try
        {
            initKoin(scenarios = scenarios) {}
        }
        catch (e: Throwable)
        {
            loadKoinModules(
                listOf(CoreKoinModule(scenarios = scenarios).mockModule(),
                       GameKoinModule().module()
                )
            )
        }
    }

    fun setApp(newApp: KoinApplication) { app = newApp }
}