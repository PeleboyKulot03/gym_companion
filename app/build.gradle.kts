plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.gymcompanion"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.gymcompanion"
        minSdk = 24
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
    packaging {
        resources.pickFirsts.add ("lib/x86/libc++_shared.so")
        resources.pickFirsts.add  ("lib/x86_64/libc++_shared.so")
        resources.pickFirsts.add  ("lib/armeabi-v7a/libc++_shared.so")
        resources.pickFirsts.add  ("lib/arm64-v8a/libc++_shared.so")
    }


//    splits {
//        abi {
//            isEnable = true
//            reset()
//            include("x86", "armeabi")
//            isUniversalApk = false
//        }
//    }
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.2.0"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database:20.2.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.android.gms:play-services-auth:20.6.0")
    implementation("com.github.bumptech.glide:glide:4.15.1")

    // The following line is optional, as the core library is included indirectly by camera-camera2
    implementation ("androidx.camera:camera-core:1.3.0-beta02")
    implementation ("androidx.camera:camera-camera2:1.3.0-beta02")
    // If you want to additionally use the CameraX Lifecycle library
    implementation ("androidx.camera:camera-lifecycle:1.3.0-beta02")
    // If you want to additionally use the CameraX VideoCapture library
    implementation ("androidx.camera:camera-video:1.3.0-beta02")
    // If you want to additionally use the CameraX View class
    implementation ("androidx.camera:camera-view:1.3.0-beta02")
    // If you want to additionally add CameraX ML Kit Vision Integration
    implementation ("androidx.camera:camera-mlkit-vision:1.3.0-beta02")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))

    implementation ("com.google.mlkit:pose-detection:18.0.0-beta3")
    implementation ("com.google.mlkit:pose-detection-accurate:18.0.0-beta3")
    implementation ("com.google.code.gson:gson:2.8.6")
    implementation ("com.google.guava:guava:27.1-android")

    implementation ("com.github.wseemann:FFmpegMediaMetadataRetriever-core:1.0.19")
    implementation ("org.jcodec:jcodec:0.2.5")
    implementation ("org.jcodec:jcodec-android:0.2.5")
    implementation ("org.jcodec:jcodec-javase:0.2.5")
    implementation ("com.arthenica:ffmpeg-kit-full:6.0-2")
}