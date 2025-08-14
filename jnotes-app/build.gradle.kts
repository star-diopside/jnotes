plugins {
    id("org.springframework.boot")
}

dependencies {
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("commons-codec:commons-codec")
    implementation("org.apache.commons:commons-lang3")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.session:spring-session-data-redis")
    runtimeOnly("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
