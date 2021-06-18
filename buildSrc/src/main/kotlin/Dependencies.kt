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
    const val COROUTINE = "1.2.1"

    const val APP_COMPAT = "1.2.0"
    const val CONSTRAINT_LAYOUT = "2.0.4"
    const val CORE = "1.3.2"
    const val LEGACY_SUPPORT = "1.0.0"

    const val FRAGMENT = "1.3.0-beta01"
    const val NAV = "2.3.4"

    const val RECYCLERVIEW = "1.2.0-alpha06"

    const val LIFECYCLE = "2.2.0"

    const val MATERIAL = "1.3.0"

    const val GLIDE = "4.10.0"

    const val GSON = "2.8.5"
    const val MOSHI = "1.8.0"
    const val RETROFIT = "2.9.0"
    const val RETROFIT_CONVERTER_GSON = "2.5.0"

    const val DATA_STORE_PREFERENCES = "1.0.0-alpha08"
    const val OK_HTTP = "4.7.2"

    const val PAGING = "3.0.0-alpha07"

    const val HILT = "2.28-alpha"
    const val HILT_COMPILER = "1.0.0-alpha02"
    const val HILT_LIFECYCLE_VIEWMODEL = "1.0.0-alpha02"

    const val SAVED_STATE = "2.3.0"

    const val ROOM = "2.3.0-alpha03"
    const val ROOM_TESTING = "1.1.1"

    const val TIMBER = "4.7.1"

    const val TEST_CORE = "2.1.0"
    const val TEST_COROUTINE = "1.3.0"
    const val TEST_JUNIT = "1.1.1"
    const val TEST_RULES_RUNNERS = "1.2.0"
    const val JUNIT4 = "4.13.1" // todo update version
    const val ESPRESSO = "3.3.0"
    const val HAMCREST = "1.3"
    const val MOCKITO = "3.3.3"
    const val ROBOLECTRIC = "4.3.1"
}

object Libs {
    val gradleTools = "com.android.tools.build:gradle:${Versions.GRADLE_TOOLS}"

    // Kotlin
    val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    val kotlinStandardLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}"

    // Testing
    val junit4 = "junit:junit:${Versions.JUNIT4}"
}