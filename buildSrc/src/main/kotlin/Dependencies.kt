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
    const val detail = ":features:detail"
    const val watchlist = ":features:watchlist"
    const val search = ":features:search"
    const val progress = ":features:progress"
    const val discovery = ":features:discovery"
}

object Core {
    const val network = ":core:network"
    const val cache = ":core:cache"
    const val views = ":core:views"
}

object UiComponents {
    const val uiComponents = ":ui-components"
}

object Common {
    const val models = ":common:models"
}

object Versions {
    const val GRADLE_TOOLS = "4.1.2" // todo update version

    const val KOTLIN = "1.5.0" // todo update version
    const val COROUTINE = "1.2.1"
    const val COROUTINE_TEST = "1.3.0" // todo update version

    const val APP_COMPAT = "1.2.0" // todo update version
    const val CONSTRAINT_LAYOUT = "2.0.4"
    const val ANDROIDX_CORE = "1.3.2" // todo update version
    const val LEGACY_SUPPORT = "1.0.0"

    const val FRAGMENT = "1.3.0-beta01" // todo update version
    const val NAVIGATION = "2.3.4" // todo update version

    const val RECYCLER_VIEW = "1.2.0-alpha06" // todo update version

    const val LIFECYCLE = "2.2.0" // todo update version

    const val GOOGLE_MATERIAL = "1.3.0"

    const val GLIDE = "4.10.0" // todo update version

    const val GSON = "2.8.5" // todo update version
    const val MOSHI = "1.8.0"
    const val RETROFIT = "2.9.0"
    const val RETROFIT_CONVERTER_GSON = "2.5.0" // todo update version

    const val DATA_STORE_PREFERENCES = "1.0.0-alpha08" // todo update version
    const val OK_HTTP = "4.7.2" // todo update version

    const val PAGING_RUNTIME = "3.0.0-alpha07" // todo update version

    const val HILT = "2.28-alpha"
    const val HILT_COMPILER = "1.0.0-alpha02"
    const val HILT_ANDROID_COMPILER = "2.37"
    const val HILT_LIFECYCLE_VIEWMODEL = "1.0.0-alpha02" // todo update version

    const val SAVED_STATE = "2.3.0"

    const val ROOM = "2.3.0" // todo update version
    const val ROOM_TESTING = "1.1.1"

    const val TIMBER = "4.7.1"

    const val TEST_CORE = "2.1.0"
    const val TEST_CORE_KTX = "1.2.0" // todo update version
    const val TEST_JUNIT = "1.1.1" // todo update version
    const val TEST_RULES_RUNNERS = "1.2.0" // todo update version
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

    val androidxAppCompat = "androidx.appcompat:appcompat:${Versions.APP_COMPAT}"
    val androidxCoreKtx = "androidx.core:core-ktx:${Versions.ANDROIDX_CORE}"
    val androidxLegacySupportv4 = "androidx.legacy:legacy-support-v4:${Versions.LEGACY_SUPPORT}"
    val googleMaterial = "com.google.android.material:material:${Versions.GOOGLE_MATERIAL}"
    val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    val recyclerView = "androidx.recyclerview:recyclerview:${Versions.RECYCLER_VIEW}"

    // Lifecycle
    val lifecycleViewModelSavedState = "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.LIFECYCLE}"
    val lifecycleKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE}"
    val lifecycleViewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
    val lifecycleLiveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}"
    val lifecycleExt = "androidx.lifecycle:lifecycle-extensions:${Versions.LIFECYCLE}"

    // Coroutines
    val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINE}"
    val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINE}"
    val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINE_TEST}"


    // Retrofit
    val retrofit = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    val okHttpLogInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.OK_HTTP}"
    val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.MOSHI}"
    val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.RETROFIT}"
    val retrofitScalarsConverter = "com.squareup.retrofit2:converter-scalars:${Versions.RETROFIT}"
    val gson = "com.google.code.gson:gson:${Versions.GSON}"
    val retrofitGsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.RETROFIT_CONVERTER_GSON}"

    // Room
    val roomKtx = "androidx.room:room-ktx:${Versions.ROOM}"
    val roomCompiler = "androidx.room:room-compiler:${Versions.ROOM}"
    val roomTesting = "android.arch.persistence.room:testing:${Versions.ROOM_TESTING}"

    // DataStore
    val dataStorePref = "androidx.datastore:datastore-preferences:${Versions.DATA_STORE_PREFERENCES}"

    // Glide
    val  glide = "com.github.bumptech.glide:glide:${Versions.GLIDE}"
    val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.GLIDE}"

    // Timber
    val timber = "com.jakewharton.timber:timber:${Versions.TIMBER}"

    // Paging
    val pagingRuntimeKtx = "androidx.paging:paging-runtime-ktx:${Versions.PAGING_RUNTIME}"

    // DI Hilt
    val hilt = "com.google.dagger:hilt-android:${Versions.HILT_ANDROID_COMPILER}"
    val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${Versions.HILT_ANDROID_COMPILER}"
    val hiltTesting = "com.google.dagger:hilt-android-testing:${Versions.HILT}"
    val hiltLifecycleViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.HILT_LIFECYCLE_VIEWMODEL}"
    val hiltCompiler = "androidx.hilt:hilt-compiler:${Versions.HILT_COMPILER}"
    val hiltPlugin = "com.google.dagger:hilt-android-gradle-plugin:${Versions.HILT_ANDROID_COMPILER}"

    // Fragments
    val fragmentTest = "androidx.fragment:fragment-testing:${Versions.FRAGMENT}"

    // Navigation
    val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"
    val navigationTest = "androidx.navigation:navigation-testing:${Versions.NAVIGATION}"
    val navigationSafeArgsPlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}"

    // ========
    // TESTING
    // ========

    // Espresso
    val espressoCore = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"
    val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.ESPRESSO}"

    // Junit 4
    val junit4 = "junit:junit:${Versions.JUNIT4}"
    val junitExt = "androidx.test.ext:junit:${Versions.TEST_JUNIT}"
    val junitExtKtx = "androidx.test.ext:junit-ktx:${Versions.TEST_JUNIT}"
    val testCoreKtx = "androidx.test:core-ktx:${Versions.TEST_CORE_KTX}"
    val testCore = "androidx.arch.core:core-testing:${Versions.TEST_CORE}"
    val testRules = "androidx.test:rules:${Versions.TEST_RULES_RUNNERS}"
    val testRunner = "androidx.test:runner:${Versions.TEST_RULES_RUNNERS}"

    // Robolectric
    val robolectric = "org.robolectric:robolectric:${Versions.ROBOLECTRIC}"

    // Hamcrest
    val hamcrestAll = "org.hamcrest:hamcrest-all:${Versions.HAMCREST}"

    // Mockito
    val mockitoCore = "org.mockito:mockito-core:${Versions.MOCKITO}"
}