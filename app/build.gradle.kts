plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

// Helper function to calculate a Version Code from a tag like "v1.2.3"
// v1.2.3 becomes 10203 (1 * 10000 + 2 * 100 + 3)
fun generateVersionCode(tag: String?): Int {
    val cleanTag = tag?.replace("v", "") ?: return 1
    val parts = cleanTag.split(".")
    if (parts.size < 2) return 1
    return try {
        val major = parts[0].toInt() * 10000
        val minor = parts[1].toInt() * 100
        val patch = if (parts.size > 2) parts[2].toInt() else 0
        major + minor + patch
    } catch (e: Exception) { 1 }
}

android {
    // Read the tag from GitHub's environment variables
    val githubTag = System.getenv("GITHUB_REF_NAME") // Only exists on GitHub Actions
    val isRelease = System.getenv("GITHUB_EVENT_NAME") == "release"

    namespace = "uk.co.liampeters.haptictoggle"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "uk.co.liampeters.haptictoggle"
        minSdk = 24
        targetSdk = 36
        versionCode = if (isRelease) generateVersionCode(githubTag) else 1
        versionName = githubTag ?: "1.0-DEBUG"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        testInstrumentationRunnerArguments["useTestStorageService"] = "true"
    }

    signingConfigs {
        create("release") {
            // These will be fed by GitHub Secrets
            storeFile = file("release.jks")
            storePassword = System.getenv("KEYSTORE_PASSWORD")
            keyAlias = System.getenv("KEY_ALIAS")
            keyPassword = System.getenv("KEY_PASSWORD")
        }
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.uiautomator)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}