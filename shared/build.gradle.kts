plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id(Dependency.MokoResources.plugin)
}

kotlin {
    android()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(Dependency.MokoResources.common)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(Dependency.MokoResources.commonTest)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(Dependency.MokoResources.android)
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

multiplatformResources {
    multiplatformResourcesPackage = "com.megabreezy.library"
    disableStaticFrameworkWarning = true
}

android {
    namespace = "ly.tithe.breezybuilds_wordle"
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
        minSdk = 24
        targetSdk = Version.androidCompileSdk
    }
}