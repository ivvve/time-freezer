plugins {
    id("maven-publish")
}

dependencies {
    implementation("io.mockk:mockk:1.13.3")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}
