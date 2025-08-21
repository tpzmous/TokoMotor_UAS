buildscript {
    repositories {
        google()
        mavenCentral()
    }
}
// Top-level build.gradle.kts
plugins {
    id("com.android.application") version "8.5.2" apply false
    id("com.android.library") version "8.5.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.10" apply false
}


tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

