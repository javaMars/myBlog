java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

plugins {
    id("java")
    id("war")
}

group = "org.thirdsprint"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("org.springframework:spring-test:6.2.1")
    testImplementation("org.hamcrest:hamcrest:2.2")

    implementation("org.springframework:spring-context:6.2.8")
    implementation("org.springframework:spring-webmvc:6.2.8")

    implementation("org.springframework.data:spring-data-jpa:3.5.1")
    implementation("org.hibernate.orm:hibernate-core:6.2.7.Final")
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")
    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")

    compileOnly("org.projectlombok:lombok:1.18.32")
    annotationProcessor("org.projectlombok:lombok:1.18.32")

    implementation("com.h2database:h2:2.2.224")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.3.RELEASE")
}

tasks.test {
    useJUnitPlatform()
}