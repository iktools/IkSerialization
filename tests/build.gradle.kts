plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(":core"))
    ksp(project(":processor"))

    testImplementation(kotlin("test"))
}
