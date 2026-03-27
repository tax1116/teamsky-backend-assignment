plugins {
    id("spring-jar-convention")
}

dependencies {
    implementation(project(":study:domain"))

    implementation(libs.spring.context)
    implementation(libs.caffeine)
}
