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
    testImplementation("org.springframework:spring-test:5.3.27")

    implementation("org.springframework:spring-context:6.2.8")
    implementation("org.springframework:spring-webmvc:6.2.8")
    implementation("org.springframework.data:spring-data-jdbc:2.4.12")

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")

    implementation("com.h2database:h2:2.2.224")
    implementation("org.thymeleaf:thymeleaf-spring6:3.1.3.RELEASE")
}

tasks.test {
    useJUnitPlatform()
}