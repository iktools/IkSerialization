plugins {
    kotlin("jvm")
    `maven-publish`
}

kotlin {
    jvmToolchain(8)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
        }
    }
}