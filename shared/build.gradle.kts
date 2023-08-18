plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id(Dependency.Realm.baseUrl) version Version.realmVersion
    id(Dependency.MokoResources.plugin)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    tasks.matching {it.name == "iosX64ProcessResources" }.configureEach()
    {
        dependsOn(tasks.getByName("generateMRcommonMain"))
    }

    cocoapods {
        summary = "Shared module for BreezyBuilds-Wordle"
        homepage = "https://github.com/breezybuildsagame/breezybuilds-wordle"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }

        /**
         * Bugfix to prevent thousands of warnings showing up in Xcode on certain machines.
         *
         * - details: https://slack-chats.kotlinlang.org/t/2913718/hi-is-anyone-else-getting-a-lot-of-warnings-in-their-ios-bui
         */
        xcodeConfigurationToNativeBuildType["Debug"] = org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType.RELEASE
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependency.Coroutines.common)
                implementation(Dependency.Koin.common)
                implementation(Dependency.MokoResources.common)
                implementation(Dependency.Realm.common)
                implementation(Dependency.DateTime.common)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(Dependency.Koin.test)
                implementation(Dependency.MokoResources.commonTest)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependency.Coroutines.android)
                implementation(Dependency.MokoResources.android)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(Dependency.Koin.test)
                implementation(Dependency.Koin.testJunit)
            }
        }
        val iosMain by sourceSets.getting
        val iosTest by sourceSets.getting
        val iosSimulatorArm64Main by sourceSets.getting
        val iosSimulatorArm64Test by sourceSets.getting

        // Set up dependencies between the source sets
        iosSimulatorArm64Main.dependsOn(iosMain)
        iosSimulatorArm64Test.dependsOn(iosTest)
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.megabreezy.library"
    disableStaticFrameworkWarning = true
}

android {
    namespace = "com.megabreezy.breezybuilds_wordle"
    compileSdk = 32

    /**
     * Bugfix for moko resources 0.20.1 on Gradle versions: 7.3 - 7.4
     *
     * - details: https://github.com/icerockdev/moko-resources/issues/353#issuecomment-1193089278
     */

    sourceSets["main"].apply {
        assets.srcDir(File(buildDir, "generated/moko/androidMain/assets"))
        res.srcDir(File(buildDir, "generated/moko/androidMain/res"))
    }

    defaultConfig {
        minSdk = Version.androidMinCompileSdk
        targetSdk = Version.androidCompileSdk
    }
}