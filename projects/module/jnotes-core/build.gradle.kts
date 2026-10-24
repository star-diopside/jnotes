plugins {
    id("java-common-conventions")
}

dependencies {
    implementation("org.jspecify:jspecify")
    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-web")
}

tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "jp.gr.java_conf.stardiopside.jnotes.core")
    }
}
