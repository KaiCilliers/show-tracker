// todo before updating all versions ensure you have enough tests to run
object Releases {
    val versionCode = 1
    val versionName = "1.0"
}

object DefaultConfig {
    val buildToolsVersion = "30.0.2" // todo update to new 30.0.3
    val appId = "com.sunrisekcdeveloper.showtracker"
    val minSdk = 23
    val targetSdk = 30
    val compileSdk = 30
}

object Modules {

}

object Versions {
    const val GRADLE_TOOLS = "4.1.2" // todo update version

    const val KOTLIN = "1.4.20" // todo update version

    const val JUNIT4 = "4.13.1" // todo update version
}

object Libs {
    val gradleTools = "com.android.tools.build:gradle:${Versions.GRADLE_TOOLS}"

    // Kotlin
    val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    val kotlinStandardLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"

    // Testing
    val junit4 = "junit:junit:${Versions.JUNIT4}"
}