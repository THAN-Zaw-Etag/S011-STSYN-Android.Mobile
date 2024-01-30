plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id ("kotlinx-serialization")
}

android {
    namespace = "com.etag.stsyn"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.etag.stsyn"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val hilt_version = "2.46"
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("com.google.dagger:hilt-android:$hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")
    //kapt ("com.google.dagger:hilt-android-compiler:$hilt_version")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    implementation("androidx.work:work-runtime-ktx:2.9.0")

    val accompanist_version = "0.33.2-alpha"
    implementation("com.google.accompanist:accompanist-pager:$accompanist_version")
    implementation("com.google.accompanist:accompanist-pager-indicators:$accompanist_version")

    // jetpack compose navigation
    val nav_version = "2.5.3"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // swipeable item
    implementation("io.github.kevinnzou:compose-swipebox:1.2.0")

    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    implementation(files("libs/API3_LIB_release_2.0.1.44.aar"))

    // constraintLayout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.6.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    implementation("androidx.compose.material3:material3:1.1.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")

    implementation ("androidx.compose.runtime:runtime-livedata:1.5.3")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation(project(":retrofit_module"))
}

kapt {
    correctErrorTypes = true
}