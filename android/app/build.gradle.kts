import java.util.Properties
import java.io.FileInputStream

val keystorePropsFile = File(project.projectDir, "keystore.properties")
val keystoreProperties = Properties().apply {
    load(FileInputStream(keystorePropsFile))
}

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("dev.flutter.flutter-gradle-plugin")
}

android {
    namespace = "com.example.white_label_app"
    compileSdk = flutter.compileSdkVersion
    ndkVersion = flutter.ndkVersion

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }

    defaultConfig {
        applicationId = "com.example.white_label_app"
        minSdk = flutter.minSdkVersion
        targetSdk = flutter.targetSdkVersion
        versionCode = flutter.versionCode
        versionName = flutter.versionName
    }

    signingConfigs {
        create("releaseConfig") {
            storeFile     = file(keystoreProperties.getProperty("STORE_FILE"))
            storePassword = keystoreProperties.getProperty("STORE_PASSWORD")
            keyAlias      = keystoreProperties.getProperty("KEY_ALIAS")
            keyPassword   = keystoreProperties.getProperty("KEY_PASSWORD")
        }
    }

    flavorDimensions += "app"
    productFlavors {
        create("dev") {
            dimension           = "app"
            resValue("string", "app_name", "MyApp Dev")
            signingConfig = signingConfigs.getByName("debug")
        }
        create("brandA") {
            dimension           = "app"
            applicationIdSuffix = ".brandA"
            versionNameSuffix   = "-brandA"
            resValue("string", "app_name", "MyApp BrandA")
            signingConfig = signingConfigs.getByName("releaseConfig")
        }
        create("brandB") {
            dimension           = "app"
            applicationIdSuffix = ".brandB"
            versionNameSuffix   = "-brandB"
            resValue("string", "app_name", "MyApp BrandB")
            signingConfig = signingConfigs.getByName("releaseConfig")
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("releaseConfig")
        }
    }
}

flutter {
    source = "../.."
}
