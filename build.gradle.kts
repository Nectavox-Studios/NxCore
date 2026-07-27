import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java-library")
    id("com.gradleup.shadow") version "9.4.1"
}

group = "com.nectavox.nxcore"
version = rootProject.version

dependencies {
    compileOnly(project(":api"))

    implementation(project(":bukkit"))
    // implementation(project(":velocity"))
}

allprojects {
    version = "1.0.0"

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/") { name = "PaperMC" }
        maven("https://repo.helpch.at/releases/") { name = "PlaceholderAPI" }
        maven("https://jitpack.io/") { name = "JitPack" }
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "com.gradleup.shadow")

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<ShadowJar> {
        relocate("dev.triumphteam.gui", "com.nectavox.nxcore.libs.gui")
    }

    dependencies {
        val lombok = "org.projectlombok:lombok:1.18.34"

        "compileOnly"(lombok)
        "annotationProcessor"(lombok)
        "testCompileOnly"(lombok)
        "testAnnotationProcessor"(lombok)
    }
}

tasks.shadowJar {
    from(project(":api").sourceSets.main.get().output)
}

tasks.build.get().dependsOn(tasks.shadowJar)