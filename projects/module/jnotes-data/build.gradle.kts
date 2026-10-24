plugins {
    id("java-common-conventions")
}

dependencies {
    api("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation(project(":projects:module:jnotes-core"))
    implementation("org.jspecify:jspecify")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "jp.gr.java_conf.stardiopside.jnotes.data")
    }
}
