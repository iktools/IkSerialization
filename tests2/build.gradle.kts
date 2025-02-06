plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(project(":core"))
    implementation(project(":tests"))
    ksp(project(":processor"))

    testImplementation(kotlin("test"))
}
