plugins {
    `java-library`
    checkstyle
    id("com.github.spotbugs")
    id("io.spring.dependency-management")
}

group = "jp.gr.java_conf.stardiopside.jnotes"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
    dependencies {
        dependency("org.webjars.npm:bootstrap-icons:1.13.2")
        dependency("org.webjars:bootstrap:5.3.8")
    }
}

dependencies {
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
}

checkstyle {
    toolVersion = "14.3.0"
    isIgnoreFailures = true
}

spotbugs {
    toolVersion = "4.10.5"
    ignoreFailures = true
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.spotbugsMain {
    reports.create("html")
}
