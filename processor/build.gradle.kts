plugins {
    kotlin("jvm")
    alias(libs.plugins.ksp)
    `maven-publish`
}

kotlin {
    jvmToolchain(8)
}

dependencies {
    implementation(project(":core"))
    implementation(libs.ksp)
    implementation(libs.bundles.kotlinpoet)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["kotlin"])
        }
    }
}