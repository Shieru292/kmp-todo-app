plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktor)
    application
}

group = "net.shieru.my_project_1"
version = "1.0.0"
application {
    mainClass.set("net.shieru.my_project_1.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    implementation(libs.ktor.serverSwagger)
    implementation(libs.ktor.serverOpenapi)
    implementation(libs.ktor.serverRoutingOpenapi)
    implementation(libs.ktor.serverContentNegotiation)
    implementation(libs.ktor.serializationKotlinxJson)
    implementation(libs.kotlinx.serialization)
    implementation(libs.ktor.serverCors)

    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.sqlite.jdbc)

    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)

    implementation(libs.arrow.core)
    implementation(libs.arrow.fx.coroutines)
}