// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }
    dependencies {
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.6")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0-Beta")
        classpath ("com.google.gms:google-services:4.4.0")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin")
        classpath ("org.jetbrains.kotlin:kotlin-android-extensions")
    }
}

plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}