import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone

plugins {
    `java-library`
    checkstyle
    id("com.github.spotbugs")
    id("io.spring.dependency-management")
    id("net.ltgt.errorprone")
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
        dependency("com.google.errorprone:error_prone_core:2.52.0")
        dependency("com.uber.nullaway:nullaway:0.14.3")
        dependency("org.webjars.npm:bootstrap-icons:1.13.2")
        dependency("org.webjars:bootstrap:5.3.8")
    }
}

dependencies {
    errorprone("com.google.errorprone:error_prone_core")
    errorprone("com.uber.nullaway:nullaway")
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

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
    options.errorprone {
        check("NullAway", CheckSeverity.ERROR)
        check("MultipleNullnessAnnotations", CheckSeverity.OFF)
        option("NullAway:OnlyNullMarked", true)
        option("NullAway:CustomContractAnnotations", "org.springframework.lang.Contract")
        option("NullAway:JSpecifyMode", true)
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.spotbugsMain {
    reports.create("html")
}
