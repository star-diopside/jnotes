dependencies {
    implementation(project(":projects:module:jnotes-core"))
    implementation(project(":projects:module:jnotes-data"))
    implementation(project(":projects:module:jnotes-service"))
    implementation("org.apache.commons:commons-lang3")
    implementation("org.jspecify:jspecify")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect")
    runtimeOnly("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    runtimeOnly("org.webjars.npm:bootstrap-icons")
    runtimeOnly("org.webjars:bootstrap")
    runtimeOnly("org.webjars:webjars-locator-lite")
}

tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "jp.gr.java_conf.stardiopside.jnotes.web")
    }
}
