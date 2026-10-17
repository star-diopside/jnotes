dependencies {
    implementation(project(":projects:module:jnotes-data"))
    implementation("org.jspecify:jspecify")
    implementation("org.springframework.boot:spring-boot-starter-batch")
}

tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "jp.gr.java_conf.stardiopside.jnotes.batch")
    }
}
