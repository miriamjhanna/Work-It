plugins {
    alias(libs.plugins.android.application)
    // If your version catalog already contains this line you can use:
    // alias(libs.plugins.google.services)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.project3_mhanna22"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.project3_mhanna22"
        minSdk = 29          // keep your current value
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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

    // --- NEW ---
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // --- Your existing libs from the catalog ---
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // --- Firebase  ---------------------------------------------------------
    // Use the BoM so every Firebase library stays at the same version
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
}
