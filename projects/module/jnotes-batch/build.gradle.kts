dependencies {
    implementation(project(":projects:module:jnotes-data"))
    implementation("commons-codec:commons-codec")
    implementation("org.jspecify:jspecify")
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework:spring-web")
}

tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "jp.gr.java_conf.stardiopside.jnotes.batch")
    }
}
