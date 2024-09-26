plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.nutri_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.nutri_app"
        minSdk = 24
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
    implementation(libs.androidx.core.ktx) // Núcleo de Android KTX
    implementation(libs.androidx.lifecycle.runtime.ktx) // KTX para Lifecycle
    implementation(libs.androidx.activity.compose) // Actividad Compose
    implementation(platform(libs.androidx.compose.bom)) // BOM para Compose
    implementation(libs.androidx.ui) // UI de Compose
    implementation(libs.androidx.ui.graphics) // Gráficos de UI de Compose
    implementation(libs.androidx.ui.tooling.preview) // Vista previa de UI de Compose
    implementation(libs.androidx.material3) // Material 3 de Compose
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.espresso.core)
    implementation(libs.androidx.constraintlayout.compose) // Navegación de Compose
    testImplementation(libs.junit) // JUnit para pruebas unitarias
    androidTestImplementation(libs.androidx.junit) // JUnit para pruebas de Android
    androidTestImplementation(libs.androidx.espresso.core) // Espresso para pruebas de UI
    androidTestImplementation(platform(libs.androidx.compose.bom)) // BOM para pruebas de Compose
    androidTestImplementation(libs.androidx.ui.test.junit4) // Pruebas de UI de Compose
    debugImplementation(libs.androidx.ui.tooling) // Herramientas de UI de Compose
    debugImplementation(libs.androidx.ui.test.manifest) // Manifest para pruebas de UI de Compose
    implementation("androidx.compose.ui:ui:")




}
