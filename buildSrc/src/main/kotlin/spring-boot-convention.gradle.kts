import kr.co.teamsky.study.build.libs

plugins {
    id("global-convention")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    testImplementation(libs.spring.boot.starter.test)
}
