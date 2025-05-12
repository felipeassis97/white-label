## Android
🗂️ **Folders**
```plaintext
project/  
├─ android/  
│  ├─ app/  
│  │  ├─ build.gradle.kts  
│  │  ├─ keystore.properties
│  │  ├─  your.keystore.jks
│  │  └─ src/  
│  │     ├─ main/  
│  │     ├─ dev/  
│  │     ├─ brandA/  
│  │     └─ brandB/  
│  └─ gradle.properties            ← (optional, local only)  
└─ lib/  
   └─ …
   ````
   -   **keystore.properties**: contains only the four generic keys.
-   **your.keystore.jks**: Sign app `.jks`.
-   **src/{flavor}/res/**: flavor-specific resources (icons, strings, layouts).

📃 **File** - `keystore.properties` 
>Place in `android/app/keystore.properties` (.gitignore):
```properties
STORE_FILE=your.keystore.jks
STORE_PASSWORD=YourKeystorePassword
KEY_ALIAS=YourKeyAlias 
KEY_PASSWORD=YourKeyPassword
```

⚙️ **Configuration** - `build.gradle.kts` 
>At the top of `android/app/build.gradle.kts`, load the properties:
```kotlin
import java.util.Properties 
import java.io.FileInputStream

val keystorePropsFile = File(project.projectDir, "keystore.properties")
val keystoreProperties = Properties().apply { 	
	load(FileInputStream(keystorePropsFile)) 
}
```

🔑 **signingConfigs**
> Inside the `android { … }` block
```kotlin
signingConfigs { 
	create("releaseConfig") { 
		storeFile = file(keystoreProperties.getProperty("STORE_FILE")) 
		storePassword = keystoreProperties.getProperty("STORE_PASSWORD") 
		keyAlias = keystoreProperties.getProperty("KEY_ALIAS")
		keyPassword = keystoreProperties.getProperty("KEY_PASSWORD") 
	} 
}
```

🎯 **productFlavors**
```kotlin
flavorDimensions += "app" 
productFlavors { 
	create("dev") { 
		dimension = "app" 
		resValue("string", "app_name", "MyApp Dev") // Uses the default debug keystore
		signingConfig = signingConfigs.getByName("debug") 
	}
	create("brandA") { 
		dimension = "app" 
		applicationIdSuffix = ".brandA" 
		versionNameSuffix = "-brandA" 
		resValue("string", "app_name", "MyApp BrandA") 
		signingConfig = signingConfigs.getByName("releaseConfig") 
	}
 
	create("brandB") { 
		dimension = "app" 
		applicationIdSuffix = ".brandB" 
		versionNameSuffix = "-brandB" 
		resValue("string", "app_name", "MyApp BrandB") 
		signingConfig = signingConfigs.getByName("releaseConfig") 
	} 
}
```
🚀 **buildTypes**
```kotlin
buildTypes { 
	getByName("debug") { // Debug builds use the automatic debug keystore 
	} 
	getByName("release") { 
		isMinifyEnabled = true 
		proguardFiles( getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro" ) // All release builds use releaseConfig 
		signingConfig = signingConfigs.getByName("releaseConfig") 
	} 
}
```
##
🎨 **Flavor-Specific Resources**
-- **App Name** in  `AndroidManifest.xml`, set:
```xml
<application android:label="@string/app_name" 
…>
```
>The `resValue("string","app_name",…)` lines in each flavor override `@string/app_name`

-- **Launcher Icons**
> Place your adaptive & legacy icon PNGs in:
```path
android/app/src/{flavor}/res/mipmap-*/ic_launcher.png 
```
> _The folder's name must have the same name of flavor_

🚀 **Build & Run Commands**
```bash
flutter run --debug --flavor=dev
```
```bash
flutter run --debug --flavor=brandA
```
```bash
flutter run --debug --flavor=brandB
```
>**_Notice_**: _When we define flavors on our project, we need to  have a "default" flavor. 
It is not possible execute only `flutter run` . n this example, we call `dev`_
