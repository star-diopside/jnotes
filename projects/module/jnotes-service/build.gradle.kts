dependencies {
    implementation(project(":projects:module:jnotes-core"))
    implementation(project(":projects:module:jnotes-data"))
    implementation("commons-codec:commons-codec")
    implementation("org.apache.commons:commons-lang3")
    implementation("org.jspecify:jspecify")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework:spring-web")
}

tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "jp.gr.java_conf.stardiopside.jnotes.service")
    }
}
