plugins {
    id("spring-jar-convention")
}

dependencies {
    api(project(":study:domain"))

    implementation(libs.spring.boot.starter)
    implementation(libs.spring.tx)
}
