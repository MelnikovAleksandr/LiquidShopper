import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.parcelize)
}

android {

    signingConfigs {
        create("release") {
            val p = Properties()
            p.load(project.rootProject.file("local.properties").reader())
            val file: String = p.getProperty("storeFile")
            val alias: String = p.getProperty("keyAlias")
            val storePas: String = p.getProperty("storePassword")
            val keyPas: String = p.getProperty("keyPassword")
            storeFile = file("\"$file\"")
            storePassword = "\"$storePas\""
            keyAlias = "\"$alias\""
            keyPassword = "\"$keyPas\""
        }
    }

    namespace = "ru.asmelnikov.liquidshopper"
    compileSdk = 37

    defaultConfig {
        applicationId = "ru.asmelnikov.liquidshopper"
        minSdk = 33
        targetSdk = 37
        versionCode = 4
        versionName = "2.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

androidComponents {
    onVariants(selector().all()) { variant ->
        variant.outputs.forEach { output ->
            if (output is com.android.build.api.variant.impl.VariantOutputImpl) {
                val baseName = "LiquidShopper"
                val buildType = variant.buildType.orEmpty()
                val fileName = "${baseName}-${buildType}-" +
                        "v${android.defaultConfig.versionName}-" +
                        "vc${android.defaultConfig.versionCode}-" +
                        "${getDateTimeFormat()}.apk"

                output.outputFileName.set(fileName)
            }
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
    }
}

dependencies {

    // default android and kotlin
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.animation)
    implementation(libs.androidx.material.icons.core)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.constraintlayout.compose)

    // splash
    implementation(libs.core.splashscreen)

    // koin
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.core)

    // windows size
    implementation(libs.material3.window.size.class1)

    // orbit
    implementation(libs.orbit.core)
    implementation(libs.orbit.viewmodel)
    implementation(libs.orbit.compose)

    // navigation
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation3)
    implementation(libs.kotlinx.serialization.core)

    // liquid
    implementation(libs.haze)
    implementation(libs.backdrop)

    // room
    implementation(libs.room)
    implementation(libs.room.ktx)
    ksp(libs.room.codegen)

    // calendar
    implementation(libs.calendar)

    // picker
    implementation(libs.datetime.wheel.picker)
    implementation(libs.kotlinx.datetime)

    // chart
    implementation(libs.charts.pie)

    // test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

fun getDateTimeFormat(): String {
    val simpleDateFormat = SimpleDateFormat("ddMMyy")
    return simpleDateFormat.format(Date())
}