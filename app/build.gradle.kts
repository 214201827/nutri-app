plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // FIrebase
    //id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.nutricionapp"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.nutricionapp"
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
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.material)
    implementation(libs.androidx.espresso.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.navigation:navigation-compose:2.8.4")
    // Dependencias de Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.jakewharton.threetenabp:threetenabp:1.4.0")
    implementation("androidx.compose.material:material-icons-extended:1.7.5")
    implementation("com.google.android.gms:play-services-base:18.5.0")
    implementation ("com.google.firebase:firebase-firestore-ktx:24.8.1")
    //subuir imagenes
    implementation("io.coil-kt.coil3:coil-compose:3.0.3")
    implementation("io.coil-kt:coil-compose:2.2.2")

    implementation ("org.apache.poi:poi:5.2.3") // Soporte para formato HSSF (Excel 97-2003)
    implementation ("org.apache.poi:poi-ooxml:5.2.3")
    implementation ("androidx.compose.material:material-icons-extended:1.4.0")







}