plugins {
    id("com.android.application")
}

android {
    namespace = "de.fraunhofer.sit.sse.valbench.base"
    compileSdk = 34

    defaultConfig {
        applicationId = "de.fraunhofer.sit.sse.valbench.base"
        minSdk = 22
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
}

dependencies {

    testImplementation("junit:junit:4.13.2")
}