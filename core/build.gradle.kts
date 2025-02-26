plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kspPlugin)
    alias(libs.plugins.hiltPlugin)
    alias(libs.plugins.roomPlugin)
}

android {
    namespace = "io.r3chain.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    // Включает функции сборки, включая пользовательские поля BuildConfig
    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            // staging API
            buildConfigField("String", "BASE_URL", "\"http://api.r3chain.io/\"")
        }
        release {
            // production API
            buildConfigField("String", "BASE_URL", "\"http://api.r3chain.io\"")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.datastore.preferences)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.room.paging)
    ksp(libs.room.compiler)

    // Unit Tests
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)

    implementation(project(":core_api"))
}

hilt {
    enableAggregatingTask = true
}
