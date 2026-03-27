plugins {
    id("spring-boot-convention")
}

dependencies {
    implementation(project(":study:api"))
    implementation(project(":study:infra:persistence"))
    implementation(project(":study:infra:cache"))

    implementation(libs.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(project(":study:domain"))

    testImplementation(libs.testcontainers.mysql)
    testImplementation(libs.testcontainers.junit)
}
