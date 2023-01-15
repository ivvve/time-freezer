import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
}

allprojects {
    group = "devson.timefreezer"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

subprojects {
    apply {
        plugin("org.jetbrains.kotlin.jvm")
    }

    dependencies {
        testImplementation(kotlin("test"))
        testImplementation("io.kotest:kotest-runner-junit5-jvm:5.5.4")
    }

    tasks.test {
        useJUnitPlatform()

        // to resolve mockk issue caused by Java module system (reference: https://github.com/mockk/mockk/blob/master/doc/md/jdk16-access-exceptions.md)
        jvmArgs(
            "--add-opens", "java.base/java.time=ALL-UNNAMED",
        )
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}
