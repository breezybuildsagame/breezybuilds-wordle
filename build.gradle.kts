plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version(Version.gradleVersion).apply(false)
    id("com.android.library").version(Version.gradleVersion).apply(false)
    kotlin("android").version(Version.kotlinVersion).apply(false)
    kotlin("multiplatform").version(Version.kotlinVersion).apply(false)
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.kotlinVersion}")
        classpath(Dependency.MokoResources.global)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
