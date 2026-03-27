plugins {
    id("spring-jar-convention")
}

dependencies {
    implementation(project(":study:domain"))

    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.jackson.databind)
    runtimeOnly(libs.mysql.connector)
}
