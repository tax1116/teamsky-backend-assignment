plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.spring.boot.gradle)
    implementation(libs.spring.dependency.management)
    implementation("com.diffplug.spotless:spotless-plugin-gradle:7.0.3")
}
