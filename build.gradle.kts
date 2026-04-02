plugins {
    java
    checkstyle
    id("com.github.spotbugs") version "6.4.8"
    id("org.springframework.boot") version "4.0.5" apply false
    id("io.spring.dependency-management") version "1.1.7"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "checkstyle")
    apply(plugin = "com.github.spotbugs")
    apply(plugin = "io.spring.dependency-management")

    group = "jp.gr.java_conf.stardiopside"
    version = "0.0.1-SNAPSHOT"

    extra["groovy.version"] = "5.0.5"

    repositories {
        mavenCentral()
    }

    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }

    dependencies {
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        testCompileOnly("org.projectlombok:lombok")
        testAnnotationProcessor("org.projectlombok:lombok")
    }

    checkstyle {
        toolVersion = "13.4.0"
        isIgnoreFailures = true
    }

    spotbugs {
        toolVersion = "4.9.8"
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
}
