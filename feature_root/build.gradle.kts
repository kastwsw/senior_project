plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kspPlugin)
    alias(libs.plugins.hiltPlugin)
}

android {
    namespace = "io.r3chain.feature.root"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
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

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtension.get()
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime.ktx)

    // navigation
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)

//    // accompanist for compose
//    implementation(libs.accompanist.systemuicontroller)
//    // Add window size utils
//    implementation("androidx.compose.material3:material3-window-size-class")

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Unit Tests
    testImplementation(libs.junit)

    implementation(project(":core_ui"))
    implementation(project(":core"))

    implementation(project(":feature_auth_nav"))
    implementation(project(":feature_inventory_nav"))
}