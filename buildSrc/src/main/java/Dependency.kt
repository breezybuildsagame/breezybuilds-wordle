object Dependency
{
    // Multi-threaded operations
    object Coroutines
    {
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.coroutinesVersion}"
        const val common = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.coroutinesVersion}"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Version.coroutinesVersion}"
    }

    // Dependency injection
    object Koin
    {
        const val android = "io.insert-koin:koin-android:${Version.koinVersion}"
        const val common = "io.insert-koin:koin-core:${Version.koinVersion}"
        const val test = "io.insert-koin:koin-test:${Version.koinVersion}"
        const val testJunit = "io.insert-koin:koin-test-junit4:${Version.koinVersion}"
        //const val ktor = "io.insert-koin:koin-ktor:$koin_version:${Constant.koinVersion}"
    }

    // Centralized resources
    object MokoResources
    {
        const val android = "dev.icerock.moko:resources-compose:${Version.mokoResourcesVersion}"
        const val common = "dev.icerock.moko:resources:${Version.mokoResourcesVersion}"
        const val commonTest = "dev.icerock.moko:resources-test:${Version.mokoResourcesVersion}"
        const val global = "dev.icerock.moko:resources-generator:${Version.mokoResourcesVersion}"
        const val plugin = "dev.icerock.mobile.multiplatform-resources"
    }

    // Local storage
    object Realm {
        const val baseUrl = "io.realm.kotlin"
        const val common = "$baseUrl:library-base:${Version.realmVersion}"

    }
}