plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    jvm()

    js {
        outputModuleName = "shared"
        browser()
        binaries.library()
        generateTypeScriptDefinitions()
        compilerOptions {
            target = "es2015"
        }
    }

    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
            implementation(libs.ktor.clientCore)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.serializationKotlinxJson)
            implementation(libs.ktor.clientSerialization)
            implementation(libs.ktor.clientContentNegotiation)
            implementation(libs.arrow.core)
            implementation(libs.arrow.fx.coroutines)
        }

        jsMain.dependencies {
            implementation(libs.ktor.clientJs)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

