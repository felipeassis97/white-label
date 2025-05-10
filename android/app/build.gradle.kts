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
        create("brandA") {
            storeFile     = file("keystores/brandA.jks")
            storePassword = project.findProperty("BRANDA_STORE_PASS") as String
            keyAlias      = project.findProperty("BRANDA_KEY_ALIAS") as String
            keyPassword   = project.findProperty("BRANDA_KEY_PASS")  as String
        }
        create("brandB") {
            storeFile     = file("keystores/brandB.jks")
            storePassword = project.findProperty("BRANDB_STORE_PASS") as String
            keyAlias      = project.findProperty("BRANDB_KEY_ALIAS") as String
            keyPassword   = project.findProperty("BRANDB_KEY_PASS")  as String
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
            signingConfig = signingConfigs.getByName("brandA")
        }
        create("brandB") {
            dimension           = "app"
            applicationIdSuffix = ".brandB"
            versionNameSuffix   = "-brandB"
            resValue("string", "app_name", "MyApp BrandB")
            signingConfig = signingConfigs.getByName("brandB")
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

flutter {
    source = "../.."
}
