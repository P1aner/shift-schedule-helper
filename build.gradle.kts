plugins {
    java
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "by.faeton"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {

    annotationProcessor("org.projectlombok:lombok")

    compileOnly("org.projectlombok:lombok")

    implementation("com.github.ben-manes.caffeine:caffeine:3.2.0")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.telegram:telegrambots-client:7.9.1")
    implementation("org.telegram:telegrambots-springboot-longpolling-starter:7.9.1")
}