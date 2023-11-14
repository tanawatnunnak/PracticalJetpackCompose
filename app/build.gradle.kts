import org.jetbrains.kotlin.konan.properties.Properties
import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    jacoco
}

// Register the main JaCoCo task to later depend on the per-variant tasks
val jacocoTestReport = tasks.register("jacocoTestReport")

tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
    }
    configure<JacocoTaskExtension> {
        excludes = listOf("jdk.internal.*")
    }
}


android {
    namespace = "com.example.practicaljetpackcompose"
    compileSdk = 33

    applicationVariants.all(closureOf<com.android.build.gradle.api.ApplicationVariant> {
        val testTaskName = "test${this@closureOf.name.capitalize()}UnitTest"

        val excludes = listOf(
            // Android
            "**/R.class",
            "**/R\$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*theme.*",
        )

        val reportTask = tasks.register("jacoco${testTaskName.capitalize()}Report", JacocoReport::class) {
            group = "Reporting"
            description = "Generate Jacoco coverage reports for the ${testTaskName.capitalize()}} build."

            dependsOn(testTaskName)

            reports {
                xml.required.set(true)
                html.required.set(true)
            }

            classDirectories.setFrom(
                files(
                    fileTree(this@closureOf.javaCompileProvider.get().destinationDir) {
                        exclude(excludes)
                    },
                    fileTree("$buildDir/tmp/kotlin-classes/${this@closureOf.name}") {
                        exclude(excludes)
                    }
                )
            )

            // Code underneath /src/{variant}/kotlin will also be picked up here
            sourceDirectories.setFrom(this@closureOf.sourceSets.flatMap { it.javaDirectories })
            executionData.setFrom(file("$buildDir/jacoco/$testTaskName.exec"))
        }

        jacocoTestReport.dependsOn(reportTask)
    })

    /*val properties = Properties().apply {
        load(project.rootProject.file("local.properties").inputStream())
    }*/

    signingConfigs {
        create("release") {
            storeFile = file("../keystore/compose_practical_keystore.jks")
            storePassword = "T@nawat!26"
            keyAlias = "ComposePractical"
            keyPassword = "T@nawat!26"
            storeFile = file("../keystore/compose_practical_keystore.jks")
            storePassword = System.getenv("SIGNING_STORE_PASSWORD")
            keyAlias = System.getenv("SIGNING_KEY_ALIAS")
            keyPassword = System.getenv("SIGNING_KEY_PASSWORD")
            /*storeFile = file(properties.getProperty("keystore_path"))
            storePassword = properties.getProperty("keystore_password")
            keyAlias = properties.getProperty("keystore_key_alias")
            keyPassword = properties.getProperty("keystore_key_password")
            */
        }
    }

    defaultConfig {
        applicationId = "com.example.practicaljetpackcompose"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
        animationsDisabled = true
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "**/attach_hotspot_windows.dll"
            excludes += "META-INF/licenses/ASM"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")


}