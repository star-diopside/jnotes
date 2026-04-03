plugins {
    id("org.springframework.boot")
}

dependencies {
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("commons-codec:commons-codec")
    implementation("org.apache.commons:commons-lang3")
    implementation("org.jspecify:jspecify")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-session-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.springframework.boot:spring-boot-starter-flyway")
    runtimeOnly("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    runtimeOnly("org.webjars.npm:bootstrap-icons")
    runtimeOnly("org.webjars:bootstrap")
    runtimeOnly("org.webjars:webjars-locator-lite")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.springframework.boot:spring-boot-starter-flyway-test")
}

tasks.test {
    useJUnitPlatform()
}
