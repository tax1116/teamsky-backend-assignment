plugins {
    id("spring-jar-convention")
}

dependencies {
    implementation(project(":study:app"))

    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.springdoc.openapi.starter.webmvc.ui)
}
