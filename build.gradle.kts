plugins {
    java
    checkstyle
    id("com.github.spotbugs") version "6.5.11"
    id("org.springframework.boot") version "4.1.1" apply false
    id("io.spring.dependency-management") version "1.1.7"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "checkstyle")
    apply(plugin = "com.github.spotbugs")
    apply(plugin = "io.spring.dependency-management")

    group = "jp.gr.java_conf.stardiopside"
    version = "0.0.1-SNAPSHOT"

    extra["lombok.version"] = "1.18.48"

    repositories {
        mavenCentral()
    }

    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
        dependencies {
            dependency("org.webjars.npm:bootstrap-icons:1.13.1")
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
        toolVersion = "14.1.0"
        isIgnoreFailures = true
    }

    spotbugs {
        toolVersion = "4.10.4"
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
