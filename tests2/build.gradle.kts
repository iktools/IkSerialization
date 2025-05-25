plugins {
    kotlin("jvm")
    alias(libs.plugins.ksp)
}

kotlin {
    jvmToolchain(8)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":core"))
    implementation(project(":tests"))
    ksp(project(":processor"))

    testImplementation(kotlin("test"))
}
