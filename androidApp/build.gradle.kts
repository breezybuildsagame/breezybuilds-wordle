plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.megabreezy.breezybuilds_wordle.android"
    compileSdk = Version.androidCompileSdk
    defaultConfig {
        applicationId = "com.megabreezy.breezybuilds_wordle.android"
        minSdk = Version.androidMinCompileSdk
        targetSdk = Version.androidCompileSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Version.composeCompilerVersion
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.compose.ui:ui-tooling-preview:${Version.jetpackComposeVersion}")
    implementation("androidx.compose.foundation:foundation:${Version.jetpackComposeVersion}")
    implementation("androidx.compose.material:material:${Version.jetpackComposeVersion}")

    implementation("androidx.activity:activity-compose:${Version.activityComposeVersion}")
    implementation("androidx.compose.runtime:runtime:${Version.jetpackComposeVersion}")
    implementation("androidx.compose.ui:ui:${Version.jetpackComposeVersion}")
    implementation("androidx.compose.material3:material3:${Version.composeMaterial3Version}")
    implementation("androidx.compose.animation:animation:${Version.jetpackComposeVersion}")
    implementation("androidx.compose.ui:ui-tooling:${Version.jetpackComposeVersion}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${Version.viewModelComposeVersion}")
    implementation("com.google.accompanist:accompanist-navigation-animation:"
                           + Version.accompanistVersion)

    implementation(Dependency.Koin.android)

    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")

    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${Version.jetpackComposeVersion}")
    debugImplementation("androidx.compose.ui:ui-test-manifest:${Version.jetpackComposeVersion}")
}