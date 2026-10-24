plugins {
    id("java-common-conventions")
    id("org.springframework.boot")
}

dependencies {
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation(project(":projects:module:jnotes-batch"))
    implementation("org.jspecify:jspecify")
    implementation("org.springframework.boot:spring-boot-starter-batch")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.springframework.boot:spring-boot-starter-flyway")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.springframework.boot:spring-boot-starter-flyway-test")
}

tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "jp.gr.java_conf.stardiopside.jnotes.batch.app")
    }
}

tasks.test {
    useJUnitPlatform()
}
